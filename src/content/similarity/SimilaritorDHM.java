package content.similarity;

import java.util.HashMap;
import java.util.Map;

import adt.frame.DistributedMap;
import adt.impl.DistributedHashMap;
import adt.impl.DistributedMapContainsResponse;
import adt.impl.DistributedMapGetResponse;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import content.core.Content;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.distributedmap.PeerWeightedLinkAddition;
import peer.graph.link.PeerLinkExistenceRequest;
import peer.graph.link.PeerLinkExistenceResponse;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.Weight;

/**
 * SimilaritorDHM is the Similaritor implemented with a Distributed Hash Map
 * Explicitly remembers sets of peers who watched the same content as this peer
 * Implicitly remembers the similarity of this peer to these peers by reweighting ...
 * ... the weighted link between them in the Peer Graph based on these similar content views
 * Explicit memory is used to help this peer's Retriever help another requesting peer find other peers who
 * ... are explicitly similar because they watched the same content and may have this specific content still held in storage
 * This will help this peer relay a Retrieve Content request onto another peer even when this peer has deleted the specific ContentFile from storage
 *
 */
public class SimilaritorDHM extends PeerToPeerActor {
    private DistributedMap<Content, SimilarViewPeers> distributedMap;
    private Map<Integer, SimilarContentViewPeerAlert> similarContentViewPeerAdditionWaitingOnContains;
    private Map<Integer, SimilarContentViewPeerAlert> similarContentViewPeerAdditionWaitingOnGets;
    private Map<Integer, ActorRef> iterationWaitingOnGets;
    private Map<UniversalId, Weight> reWeightingWaitingOnPeerLinkExistenceResponse;
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof SimilarContentViewPeerAlert) {
            SimilarContentViewPeerAlert alert = (SimilarContentViewPeerAlert) message;
            this.processSimilarContentViewPeerAlert(alert);
        }
        else if (message instanceof PeerLinkExistenceResponse) {
            PeerLinkExistenceResponse response = (PeerLinkExistenceResponse) message;
            this.processPeerLinkExistenceResponse(response);
        }
        else if (message instanceof SimilarContentViewPeerRequest) {
            SimilarContentViewPeerRequest request = (SimilarContentViewPeerRequest) message;
            this.processSimilarContentViewPeerRequest(request);
        }
        else if (message instanceof DistributedMapContainsResponse) {
            DistributedMapContainsResponse response = (DistributedMapContainsResponse) message;
            this.processContainsResponse(response);
        }
        else if (message instanceof DistributedMapGetResponse) {
            DistributedMapGetResponse response = (DistributedMapGetResponse) message;
            this.processGetResponse(response);
        }
    }
    
    /**
     * Initialise the Distributed Map and all the maps that hold information for the numbered requests while the request is in progress
     */
    protected void initialise() {
        this.distributedMap = new DistributedHashMap<Content, SimilarViewPeers>();
        this.distributedMap.initialise(Content.class, SimilarViewPeers.class, getContext(), getSelf(), super.peerId);
        this.similarContentViewPeerAdditionWaitingOnContains = new HashMap<Integer, SimilarContentViewPeerAlert>();
        this.similarContentViewPeerAdditionWaitingOnGets = new HashMap<Integer, SimilarContentViewPeerAlert>();
        this.iterationWaitingOnGets = new HashMap<Integer, ActorRef>();
        this.reWeightingWaitingOnPeerLinkExistenceResponse = new HashMap<UniversalId, Weight>();
    }
    
    /**
     * Will begin the process of storing information on this content view and the peers who watched it
     * Will also begin attempts to re-weight the links in the peer graph between this peer and these peers who watched the same content
     * @param alert
     */
    protected void processSimilarContentViewPeerAlert(SimilarContentViewPeerAlert alert) {
        UniversalId similarViewPeerId = alert.getSimilarViewPeerId();
        
        int requestNum = this.distributedMap.requestContains(alert.getSimilarViewContent());
        this.similarContentViewPeerAdditionWaitingOnContains.put(requestNum, alert);
        
        ActorSelection peerLinker = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        PeerLinkExistenceRequest existenceRequest = new PeerLinkExistenceRequest(similarViewPeerId);
        peerLinker.tell(existenceRequest, getSelf());
        Weight weightToGive = alert.getWeightToGive();
        this.reWeightingWaitingOnPeerLinkExistenceResponse.put(similarViewPeerId, weightToGive);
    }
    
    /**
     * Gets the set of similar view peer IDs from the distributed hash map for iteration through
     * @param request
     */
    protected void processSimilarContentViewPeerRequest(SimilarContentViewPeerRequest request) {
        Content content = request.getContent();
        int requestNum = this.distributedMap.requestGet(content);
        this.iterationWaitingOnGets.put(requestNum, getSender());
    }
    
    /**
     * If the distributed hash map doesn't already contain this content and associated Peer IDs who have viewed it ...
     * ... then this creates a new set of Peer IDs who have watched this content and adds this Peer ID
     * If the map already contains this content then the peer ID will be added to the existing set ...
     * ... after it is gotten by a Get request
     * 
     * @param response
     */
    protected void processContainsResponse(DistributedMapContainsResponse response) {
        int requestNum = response.getRequestNum();
        SimilarContentViewPeerAlert alert = this.similarContentViewPeerAdditionWaitingOnContains.remove(requestNum);
        Content similarViewContent = alert.getSimilarViewContent();
        boolean contains = response.contains();
        if (contains) {
            int requestNumForGet = this.distributedMap.requestGet(similarViewContent);
            this.similarContentViewPeerAdditionWaitingOnGets.put(requestNumForGet, alert);
        }
        else {
            UniversalId similarViewContentPeerId = alert.getSimilarViewPeerId();
            SimilarViewPeers similarViewPeers = new SimilarViewPeers();
            similarViewPeers.add(similarViewContentPeerId);
            this.distributedMap.requestAdd(similarViewContent, similarViewPeers);
        }
    }
    
    /**
     * In the case of the Get request being for adding a new Peer ID to the set of Peer IDs who viewed this content ...
     * ... the Peer ID will be added to the set of Peer IDs who viewed the same content
     * In the case of the Get request being for an iteration through the set then ...
     * ... the set will be iterated through and returned to requester
     * @param response
     */
    protected void processGetResponse(DistributedMapGetResponse response) {
        int requestNum = response.getRequestNum();
        Content similarViewedContent = this.distributedMap.getGetKey(response);
        SimilarViewPeers similarViewPeers = this.distributedMap.getGetValue(response);
        if (this.similarContentViewPeerAdditionWaitingOnGets.containsKey(requestNum)) {
            SimilarContentViewPeerAlert alert = this.similarContentViewPeerAdditionWaitingOnContains.remove(requestNum);
            UniversalId similarViewPeerId = alert.getSimilarViewPeerId();
            similarViewPeers.add(similarViewPeerId);
        }
        else if (this.iterationWaitingOnGets.containsKey(requestNum)) {
            ActorRef requester = this.iterationWaitingOnGets.remove(requestNum);
            for (UniversalId similarViewPeerId : similarViewPeers) {
                SimilarContentViewPeerResponse similarViewPeerResponse = new SimilarContentViewPeerResponse(similarViewedContent, similarViewPeerId);
                requester.tell(similarViewPeerResponse, getSelf());
            }
        }
        else throw new RuntimeException();
    }
    
    /**
     * Depending on the existence or not of a link in the Peer Graph the link is either re-weighted or created
     * @param response
     */
    protected void processPeerLinkExistenceResponse(PeerLinkExistenceResponse response) {
        ActorRef peerLinker = getSender();
        UniversalId linkedPeerId = response.getLinkToCheckPeerId();
        boolean exists = response.isLinkInExistence();
        Weight weight = this.reWeightingWaitingOnPeerLinkExistenceResponse.remove(linkedPeerId);
        if (exists) {
            LocalWeightUpdateRequest request = new LocalWeightUpdateRequest(linkedPeerId, weight);
            peerLinker.tell(request, getSelf());
        }
        else {
            PeerWeightedLinkAddition addition = new PeerWeightedLinkAddition(linkedPeerId, weight);
            peerLinker.tell(addition, getSelf());
        }
    }
}
