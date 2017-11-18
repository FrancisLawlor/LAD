package content.recommend;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import peer.graph.link.PeerLinkResponse;
import peer.graph.link.PeerLinksRequest;
import peer.graph.weight.WeightRequest;
import peer.graph.weight.WeightResponse;

/**
 * Aggregates Recommendations from Peers
 * Based on theoretical weighted links between this peer and others
 * PeerLinker is asked for the Peer IDs of the links
 * Sends Recommendation Request to Peers based on Peer IDs
 * Weighters corresponding to these Peer IDs are asked for their weight
 * Using Weight we weight the recommendations retrieved from peers
 *
 */
public class PeerRecommendationAggregator extends UntypedActor {
    private Map<String, PeerRecommendation> recommendations;
    private Map<String, WeightResponse> weights;
    private long startTime;
    
    private static final long timeOut = 60000;
    
    public PeerRecommendationAggregator() {
        this.recommendations = new HashMap<String, PeerRecommendation>();
        this.startTime = System.currentTimeMillis();
    }
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof RecommendationsForUserRequest) {
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
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    /**
     * Sends a request to the PeerLinker first
     * @param request
     */
    protected void processRecommendationsForUserRequest(RecommendationsForUserRequest request) {
        this.sendPeerLinksRequest();
    }
    
    /**
     * Sends request to the PeerLinker for Peer IDs of the peers 
     * linked to this peer
     */
    private void sendPeerLinksRequest() {
        PeerLinksRequest request = new PeerLinksRequest();
        
        ActorSelection peerLinker = getContext().actorSelection("user/peerLinker");
        peerLinker.tell(request, getSelf());
    }
    
    /**
     * Uses Peer ID from response to send a Peer Recommendation Request
     * @param peerLinkResponse
     */
    protected void processPeerLinkResponse(PeerLinkResponse peerLinkResponse) {
        String peerId = peerLinkResponse.getPeerId();
        this.sendPeerRecommendationRequest(peerId);
    }
    
    /**
     * Asks a Peer for its Recommendations
     */
    private void sendPeerRecommendationRequest(String peerId) {
        
    }
    
    /**
     * Records the Peer Recommendation in the Actors State for later weighting
     * Sends out Weight Request
     * @param peerRecommendation
     */
    protected void processPeerRecommendation(PeerRecommendation peerRecommendation) {
        String peerId = "To Do"; // To Do:
        // Figure out ID system for Peers that works with AKKA ActorRef and Apache Camel URIs
        this.recommendations.put(peerId, peerRecommendation);
        this.sendPeerLinkWeightRequest(peerId);
    }
    
    /**
     * Asks Weighter what Weight this Peer's Link should have in aggregate Recommendations
     */
    private void sendPeerLinkWeightRequest(String peerId) {
        WeightRequest request = new WeightRequest(peerId);
        
        ActorSelection peerWeighter = getContext().actorSelection("user/weighter_" + peerId);
        peerWeighter.tell(request, getSelf());
    }
    
    /**
     * Weighs the Peer Recommendation by the weight of the 
     * theoretical link between it and this peer
     */
    protected void processWeightResponse(WeightResponse response) {
        String peerId = response.getPeerId();
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
        if (this.weights.size() == this.recommendations.size()) {
            timeToRecommend = true;
        }
        else if (System.currentTimeMillis() - this.startTime > this.timeOut) {
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
        Set<Entry<String, PeerRecommendation>> recommendSet = this.recommendations.entrySet();
        Iterator<Entry<String, PeerRecommendation>> recommendIt = recommendSet.iterator();
        
        while (recommendIt.hasNext()) {
            Entry<String, PeerRecommendation> entry = recommendIt.next();
            String peerId = entry.getKey();
            PeerRecommendation peerRecommendation = entry.getValue();
            
            WeightResponse weightResponse = this.weights.get(peerId);
            if (weightResponse != null) {
                double weight = weightResponse.getLinkWeight();
                
                // Do something now with Peer Recommendation and Weight
            }
        }
    }
}
