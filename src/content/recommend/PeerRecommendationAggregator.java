package content.recommend;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.PoisonPill;
import content.recommend.heuristic.AggregationHeuristic;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.core.xcept.UnknownMessageException;
import peer.graph.link.PeerLinkResponse;
import peer.graph.link.PeerLinksRequest;
import peer.graph.weight.Weight;
import peer.graph.weight.WeightRequest;
import peer.graph.weight.WeightResponse;

/**
 * Aggregates Recommendations from Peers
 * Based on the idea of this peer being linked to others in a weighted graph
 * PeerLinker is asked for the Peer IDs of the links
 * Sends Recommendation Request to Peers based on Peer IDs
 * Weighters corresponding to these Peer IDs are asked for their weight
 * Using Weight we weight the recommendations retrieved from peers
 *
 */
public class PeerRecommendationAggregator extends PeerToPeerActor {
    private UniversalId id;
    private AggregationHeuristic heuristic;
    private Map<UniversalId, PeerRecommendation> recommendations;
    private Map<UniversalId, WeightResponse> weights;
    private int peerRecommendationRequestsSentOut;
    private long startTime;
    
    private static final long timeOut = 60000;
    
    /**
     * Creates a map of Universal IDs to peer recommendations
     * Creates a map to the weight of these peer recommendations
     */
    public PeerRecommendationAggregator() {
        this.recommendations = new HashMap<UniversalId, PeerRecommendation>();
        this.weights = new HashMap<UniversalId, WeightResponse>();
        this.peerRecommendationRequestsSentOut = 0;
    }
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof PeerRecommendationAggregatorInit) {
            PeerRecommendationAggregatorInit init = (PeerRecommendationAggregatorInit) message;
            this.heuristic = init.getHeuristic();
            this.startTime = System.currentTimeMillis();
        }
        else if (message instanceof RecommendationsForUserRequest) {
            RecommendationsForUserRequest request = 
                    (RecommendationsForUserRequest) message;
            this.processRecommendationsForUserRequest(request);
        }
        else if (message instanceof PeerLinkResponse) {
            PeerLinkResponse peerLinkResponse = 
                    (PeerLinkResponse) message;
            this.processPeerLinkResponse(peerLinkResponse);
        }
        else if (message instanceof PeerRecommendation) {
            PeerRecommendation recommendation = 
                    (PeerRecommendation) message;
            this.processPeerRecommendation(recommendation);
        }
        else if (message instanceof WeightResponse) {
            WeightResponse response = 
                    (WeightResponse) message;
            this.processWeightResponse(response);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Sends a request to the PeerLinker first
     * @param request
     */
    protected void processRecommendationsForUserRequest(RecommendationsForUserRequest request) {
        this.id = request.getUserPeerId();
        this.sendPeerLinksRequest();
    }
    
    /**
     * Sends request to the PeerLinker for Peer IDs of the peers 
     * linked to this peer
     */
    private void sendPeerLinksRequest() {
        PeerLinksRequest request = new PeerLinksRequest();
        
        ActorSelection peerLinker = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerLinker.tell(request, getSelf());
    }
    
    /**
     * Uses Peer ID from response to send a Peer Recommendation Request
     * @param peerLinkResponse
     */
    protected void processPeerLinkResponse(PeerLinkResponse peerLinkResponse) {
        UniversalId peerId = peerLinkResponse.getPeerId();
        this.sendPeerRecommendationRequest(peerId);
    }
    
    /**
     * Asks a Peer for its Recommendations
     */
    private void sendPeerRecommendationRequest(UniversalId peerId) {
        PeerRecommendationRequest request = new PeerRecommendationRequest(this.id, peerId);
        
        ActorSelection communicator = getContext().actorSelection(ActorPaths.getPathToOutComm());
        communicator.tell(request, getSelf());
        
        this.peerRecommendationRequestsSentOut++;
    }
    
    /**
     * Records the Peer Recommendation in the Actors State for later weighting
     * Sends out Weight Request
     * @param peerRecommendation
     */
    protected void processPeerRecommendation(PeerRecommendation peerRecommendation) {
        UniversalId peerId = peerRecommendation.getPeerId();
        this.recommendations.put(peerId, peerRecommendation);
        this.sendPeerLinkWeightRequest(peerId);
    }
    
    /**
     * Asks Weighter what Weight this Peer's Link should have in aggregate Recommendations
     */
    protected void sendPeerLinkWeightRequest(UniversalId peerId) {
        WeightRequest request = new WeightRequest(peerId);
        
        ActorSelection peerWeighter = getContext().actorSelection(ActorPaths.getPathToWeighter(peerId));
        peerWeighter.tell(request, getSelf());
    }
    
    /**
     * Weighs the Peer Recommendation by the weight of the 
     * theoretical link between it and this peer
     */
    protected void processWeightResponse(WeightResponse response) {
        UniversalId peerId = response.getLinkedPeerId();
        this.weights.put(peerId, response);
        if (this.isTimeToRecommend()) {
            this.aggregatePeerRecommendations();
        }
    }
    
    /**
     * Determines if all weights are back or timeOut has occurred
     * Recommends if either case is true
     * @return
     */
    private boolean isTimeToRecommend() {
        boolean timeToRecommend;
        if (this.recommendations.size() == this.peerRecommendationRequestsSentOut
                && this.weights.size() == this.peerRecommendationRequestsSentOut) {
            timeToRecommend = true;
        }
        else if (System.currentTimeMillis() - this.startTime > timeOut) {
            timeToRecommend = true;
        }
        else {
            timeToRecommend = false;
        }
        return timeToRecommend;
    }
    
    /**
     * Aggregates all Peer Recommendations
     * Weighted by weight values from WeightResponses
     * Weights are the theoretical similarity of peers
     */
    protected void aggregatePeerRecommendations() {
        List<WeightedPeerRecommendation> weightedPeerRecommendations = new LinkedList<WeightedPeerRecommendation>();
        
        Iterator<Entry<UniversalId, PeerRecommendation>> recommendIt = this.recommendations.entrySet().iterator();
        while (recommendIt.hasNext()) {
            Entry<UniversalId, PeerRecommendation> entry = recommendIt.next();
            UniversalId peerId = entry.getKey();
            PeerRecommendation peerRecommendation = entry.getValue();
            
            WeightResponse weightResponse = this.weights.get(peerId);
            if (weightResponse != null) {
                Weight weight = weightResponse.getLinkWeight();
                WeightedPeerRecommendation weightedRecommendation = new WeightedPeerRecommendation(peerRecommendation, weight);
                weightedPeerRecommendations.add(weightedRecommendation);
            }
        }
        RecommendationsForUser forUser = this.heuristic.getRecommendationsForUser(weightedPeerRecommendations);
        
        ActorSelection viewer = getContext().actorSelection(ActorPaths.getPathToViewer());
        viewer.tell(forUser, getSelf());
        
        getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
