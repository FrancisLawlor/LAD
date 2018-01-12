package peer.graph.distributedmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adt.frame.DistributedMap;
import adt.impl.DistributedHashMap;
import adt.impl.DistributedMapAdditionResponse;
import adt.impl.DistributedMapContainsResponse;
import adt.impl.DistributedMapGetResponse;
import adt.impl.DistributedMapIterationResponse;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.core.xcept.UnknownMessageException;
import peer.core.xcept.WeightUpdateForNonExistentLinkException;
import peer.data.BackedUpPeerLinkResponse;
import peer.data.BackupPeerLinkRequest;
import peer.graph.link.PeerLinkExistenceRequest;
import peer.graph.link.PeerLinkExistenceResponse;
import peer.graph.link.PeerLinkResponse;
import peer.graph.link.PeerLinksRequest;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.PeerWeightUpdateRequest;
import peer.graph.weight.Weight;
import peer.graph.weight.WeightRequest;
import peer.graph.weight.WeightResponse;

/**
 * Represents theoretical similarity between peers by weighted links
 * Handles Peer graph weighted links in one actor
 * Distributed Hash Map used to map peer ID links to weights
 *
 */
public class PeerWeightedLinkor extends PeerToPeerActor {
    private DistributedMap<UniversalId, Weight> distributedMap;
    private Map<Integer, PeerWeightedLinkAddition> localAdditionsWaitingOnContains;
    private Map<Integer, PeerWeightedLinkAddition> localAdditionsWaitingOnGets;
    private Map<Integer, RemotePeerWeightedLinkAddition> remoteAdditionsWaitingOnContains;
    private Map<Integer, RemotePeerWeightedLinkAddition> remoteAdditionsWaitingOnGets;
    private Map<Integer, ExistenceResponder> existenceRequestsWaitingOnContains;
    private Map<Integer, LocalWeightUpdateRequest> localWeightUpdateWaitingOnContains;
    private Map<Integer, LocalWeightUpdateRequest> localWeightUpdateWaitingOnGets;
    private Map<Integer, PeerWeightUpdateRequest> remoteWeightUpdateWaitingOnContains;
    private Map<Integer, PeerWeightUpdateRequest> remoteWeightUpdateWaitingOnGets;
    private Set<Integer> pendingAdditions;
    private Map<Integer, ActorRef> pendingWeightRequests;
    private Map<Integer, ActorRef> pendingIterationRequests;
    
    /**
     * Encapsulates the information for the existenceRequestsWaitingOnContains map
     *
     */
    private class ExistenceResponder {
        ActorRef requester;
        PeerLinkExistenceRequest request;
        ExistenceResponder(ActorRef requester, PeerLinkExistenceRequest request) {
            this.requester = requester;
            this.request = request;
        }
    }
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
            this.initialise();
        }
        else if (message instanceof PeerWeightedLinkAddition) {
            PeerWeightedLinkAddition addition = (PeerWeightedLinkAddition) message;
            this.processPeerWeightedLinkAddition(addition);
        }
        else if (message instanceof RemotePeerWeightedLinkAddition) {
            RemotePeerWeightedLinkAddition addition = (RemotePeerWeightedLinkAddition) message;
            this.processRemotePeerWeightedLinkAddition(addition);
        }
        else if (message instanceof PeerLinkExistenceRequest) {
            PeerLinkExistenceRequest request = (PeerLinkExistenceRequest) message;
            this.processPeerLinkExistenceRequest(request);
        }
        else if (message instanceof PeerLinksRequest) {
            PeerLinksRequest peerLinksRequest = (PeerLinksRequest) message;
            this.processPeerLinksRequest(peerLinksRequest);
        }
        else if (message instanceof WeightRequest) {
            WeightRequest weightRequest = (WeightRequest) message;
            this.processWeightRequest(weightRequest);
        }
        else if (message instanceof LocalWeightUpdateRequest) {
            LocalWeightUpdateRequest weightUpdateRequest = (LocalWeightUpdateRequest) message;
            this.processLocalWeightUpdateRequest(weightUpdateRequest);
        }
        else if (message instanceof PeerWeightUpdateRequest) {
            PeerWeightUpdateRequest weightUpdateRequest = (PeerWeightUpdateRequest) message;
            this.processPeerWeightUpdateRequest(weightUpdateRequest);
        }
        else if (message instanceof DistributedMapAdditionResponse) {
            DistributedMapAdditionResponse response = (DistributedMapAdditionResponse) message;
            this.processAdditionResponse(response);
        }
        else if (message instanceof DistributedMapContainsResponse) {
            DistributedMapContainsResponse response = (DistributedMapContainsResponse) message;
            this.processContainsResponse(response);
        }
        else if (message instanceof DistributedMapGetResponse) {
            DistributedMapGetResponse response = (DistributedMapGetResponse) message;
            this.processGetResponse(response);
        }
        else if (message instanceof DistributedMapIterationResponse) {
            DistributedMapIterationResponse response = (DistributedMapIterationResponse) message;
            this.processIterationResponse(response);
        }
        else if (message instanceof BackedUpPeerLinkResponse) {
            BackedUpPeerLinkResponse response = (BackedUpPeerLinkResponse) message;
            this.processBackedUpPeerLinkResponse(response);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Initialise the Peer Weighted Linkor with a Distributed Hash Map
     * Initialise with maps and sets that hold requests waiting for responses with the correct request number
     */
    public void initialise() {
        this.distributedMap = new DistributedHashMap<UniversalId, Weight>();
        this.distributedMap.initialise(UniversalId.class, Weight.class, getContext(), getSelf(), super.peerId);
        this.localAdditionsWaitingOnContains = new HashMap<Integer, PeerWeightedLinkAddition>();
        this.localAdditionsWaitingOnGets = new HashMap<Integer, PeerWeightedLinkAddition>();
        this.remoteAdditionsWaitingOnContains = new HashMap<Integer, RemotePeerWeightedLinkAddition>();
        this.remoteAdditionsWaitingOnGets = new HashMap<Integer, RemotePeerWeightedLinkAddition>();
        this.existenceRequestsWaitingOnContains = new HashMap<Integer, ExistenceResponder>();
        this.localWeightUpdateWaitingOnContains = new HashMap<Integer, LocalWeightUpdateRequest>();
        this.localWeightUpdateWaitingOnGets = new HashMap<Integer, LocalWeightUpdateRequest>();
        this.remoteWeightUpdateWaitingOnContains = new HashMap<Integer, PeerWeightUpdateRequest>();
        this.remoteWeightUpdateWaitingOnGets = new HashMap<Integer, PeerWeightUpdateRequest>();
        this.pendingAdditions = new HashSet<Integer>();
        this.pendingWeightRequests = new HashMap<Integer, ActorRef>();
        this.pendingIterationRequests = new HashMap<Integer, ActorRef>();
    }
    
    /**
     * Check if the map contains the link already before adding
     * Contains response from the distributed map will determine what to do next
     * This local peer will now need to send a request to the other peer to ask it to add the same link
     * RemotePeerWeightedLinkAddition is required to be sent to tell remote peer to add this link
     * This request keeps the weighted links of the peer graphs consistent from both perspectives
     * @param addition
     */
    protected void processPeerWeightedLinkAddition(PeerWeightedLinkAddition addition) {
        int requestNum = this.distributedMap.requestContains(addition.getLinkPeerId());
        this.localAdditionsWaitingOnContains.put(requestNum, addition);
        RemotePeerWeightedLinkAddition otherPeerAddition = new RemotePeerWeightedLinkAddition(super.peerId, addition.getLinkPeerId(), addition.getLinkWeight());
        ActorSelection outComm = getContext().actorSelection(ActorPaths.getPathToOutComm());
        outComm.tell(otherPeerAddition, getSelf());
    }
    
    /**
     * Check if the map contains the link already before adding
     * Contains response from the distributed map will determine what to do next
     * This is a remote peer requesting a link be added to keep the weighted link consistent on both ends
     * @param addition
     */
    protected void processRemotePeerWeightedLinkAddition(RemotePeerWeightedLinkAddition addition) {
        int requestNum = this.distributedMap.requestContains(addition.getLinkPeerId());
        this.remoteAdditionsWaitingOnContains.put(requestNum, addition);
    }
    
    /**
     * Asks the Distributed Map if it contains the link
     * Saves the request and requester information for responding upon receipt of the contains response
     * @param request
     */
    protected void processPeerLinkExistenceRequest(PeerLinkExistenceRequest request) {
        UniversalId linkToCheckPeerId = request.getLinkToCheckPeerId();
        int requestNum = this.distributedMap.requestContains(linkToCheckPeerId);
        ExistenceResponder responder = new ExistenceResponder(getSender(), request);
        this.existenceRequestsWaitingOnContains.put(requestNum, responder);
    }
    
    /**
     * Requests all the peer links by requesting an iteration of the distributed hash map
     * @param request
     */
    protected void processPeerLinksRequest(PeerLinksRequest request) {
        int requestNum = this.distributedMap.requestIterator();
        this.pendingIterationRequests.put(requestNum, getSender());
    }
    
    /**
     * Request the weight of a link
     * @param request
     */
    protected void processWeightRequest(WeightRequest request) {
        int requestNum = this.distributedMap.requestGet(request.getLinkedPeerId());
        this.pendingWeightRequests.put(requestNum, getSender());
    }
    
    /**
     * Asks if the Distributed Map contains the link before attempting to update it
     * Request waits for request with this request number to return as a contains response
     * @param request
     */
    protected void processLocalWeightUpdateRequest(LocalWeightUpdateRequest request) {
        int requestNum = this.distributedMap.requestContains(request.getLinkedPeerId());
        this.localWeightUpdateWaitingOnContains.put(requestNum, request);
    }
    
    /**
     * Asks if the Distributed Map contains the link before attempting to update it
     * Request waits for request with this request number to return as a contains response
     * @param request
     */
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest request) {
        int requestNum = this.distributedMap.requestContains(request.getUpdateRequestingPeerId());
        this.remoteWeightUpdateWaitingOnContains.put(requestNum, request);
    }
    
    /**
     * Removes the pending addition from the pending map
     * @param response
     */
    protected void processAdditionResponse(DistributedMapAdditionResponse response) {
        this.pendingAdditions.remove(response.getRequestNum());
    }
    
    /**
     * Figures out which request was waiting on this Contains response
     * An addition request will ask to get the current value if the map contains the key already,
     * ... else it will add the new link immediately
     * An existence request will reply with the existence or non-existence to the requester
     * A weight update request will ask to get the current value if the map contains the key already, 
     * ... else it will throw an exception since the link should already exist
     * @param response
     */
    protected void processContainsResponse(DistributedMapContainsResponse response) {
        int requestNum = response.getRequestNum();
        if (this.localAdditionsWaitingOnContains.containsKey(requestNum)) {
            PeerWeightedLinkAddition addition = this.localAdditionsWaitingOnContains.remove(requestNum);
            boolean containsLink = response.contains();
            if (containsLink) {
                int requestNumForGet = this.distributedMap.requestGet(addition.getLinkPeerId());
                this.localAdditionsWaitingOnGets.put(requestNumForGet, addition);
            }
            else {
                this.distributedMap.requestAdd(addition.getLinkPeerId(), addition.getLinkWeight());
                this.backupPeerWeightedLink(new PeerWeightedLink(addition.getLinkPeerId(), addition.getLinkWeight()));
            }
        }
        else if (this.remoteAdditionsWaitingOnContains.containsKey(requestNum)) {
            RemotePeerWeightedLinkAddition addition = this.remoteAdditionsWaitingOnContains.remove(requestNum);
            boolean containsLink = response.contains();
            if (containsLink) {
                int requestNumForGet = this.distributedMap.requestGet(addition.getLinkPeerId());
                this.remoteAdditionsWaitingOnGets.put(requestNumForGet, addition);
            }
            else {
                this.distributedMap.requestAdd(addition.getLinkPeerId(), addition.getLinkWeight());
                this.backupPeerWeightedLink(new PeerWeightedLink(addition.getLinkPeerId(), addition.getLinkWeight()));
            }
        }
        else if (this.existenceRequestsWaitingOnContains.containsKey(requestNum)) {
            ExistenceResponder existenceResponse = this.existenceRequestsWaitingOnContains.remove(requestNum);
            ActorRef requester = existenceResponse.requester;
            PeerLinkExistenceRequest request = existenceResponse.request;
            PeerLinkExistenceResponse existsResponse = new PeerLinkExistenceResponse(request.getLinkToCheckPeerId(), response.contains());
            requester.tell(existsResponse, getSelf());
        }
        else if (this.localWeightUpdateWaitingOnContains.containsKey(requestNum)) {
            LocalWeightUpdateRequest request = this.localWeightUpdateWaitingOnContains.remove(requestNum);
            if (response.contains()) {
                int requestNumForGet = this.distributedMap.requestGet(request.getLinkedPeerId());
                this.localWeightUpdateWaitingOnGets.put(requestNumForGet, request);
            }
            else throw new WeightUpdateForNonExistentLinkException(request.getLinkedPeerId(), request.getNewWeight());
        }
        else if (this.remoteWeightUpdateWaitingOnContains.containsKey(requestNum)) {
            PeerWeightUpdateRequest request = this.remoteWeightUpdateWaitingOnContains.remove(requestNum);
            if (response.contains()) {
                int requestNumForGet = this.distributedMap.requestGet(request.getUpdateRequestingPeerId());
                this.remoteWeightUpdateWaitingOnGets.put(requestNumForGet, request);
            }
            else throw new WeightUpdateForNonExistentLinkException(request.getUpdateRequestingPeerId(), request.getNewWeight());
        }
        else throw new RuntimeException();
    }
    
    /**
     * Figures out which request is waiting on this Get response
     * If it's an addition request then the new addition weight will be added to the gotten weight
     * If it's a weight update request then the weight update weight will be added to the gotten weight and,
     * ... a PeerWeightUpdateRequest will be sent to the corresponding peer to tell them to update the weight similarly on their end
     * ... which will keep the weighted link consistent on both sides of our theoretical graph
     * If it's a weight request it gets the weight and returns it to the requester in a WeightResponse
     * @param response
     */
    protected void processGetResponse(DistributedMapGetResponse response) {
        int requestNum = response.getRequestNum();
        if (this.localAdditionsWaitingOnGets.containsKey(requestNum)) {
            PeerWeightedLinkAddition request = this.localAdditionsWaitingOnGets.remove(requestNum);
            Weight existingWeight = this.distributedMap.getGetValue(response);
            Weight newWeight = request.getLinkWeight();
            existingWeight.add(newWeight);
            this.backupPeerWeightedLink(new PeerWeightedLink(request.getLinkPeerId(), existingWeight));
        }
        else if (this.remoteAdditionsWaitingOnGets.containsKey(requestNum)) {
            RemotePeerWeightedLinkAddition request = this.remoteAdditionsWaitingOnGets.remove(requestNum);
            Weight existingWeight = this.distributedMap.getGetValue(response);
            Weight newWeight = request.getLinkWeight();
            existingWeight.add(newWeight);
            this.backupPeerWeightedLink(new PeerWeightedLink(request.getLinkPeerId(), existingWeight));
        }
        else if (this.localWeightUpdateWaitingOnGets.containsKey(requestNum)) {
            LocalWeightUpdateRequest request = this.localWeightUpdateWaitingOnGets.remove(requestNum);
            Weight existingWeight = this.distributedMap.getGetValue(response);
            Weight newWeight = request.getNewWeight();
            existingWeight.add(newWeight);
            this.backupPeerWeightedLink(new PeerWeightedLink(request.getLinkedPeerId(), existingWeight));
            
            PeerWeightUpdateRequest peerUpdateRequest = new PeerWeightUpdateRequest(super.peerId, request.getLinkedPeerId(), request.getNewWeight());
            ActorSelection outComm = getContext().actorSelection(ActorPaths.getPathToOutComm());
            outComm.tell(peerUpdateRequest, getSelf());
        }
        else if (this.remoteWeightUpdateWaitingOnGets.containsKey(requestNum)) {
            PeerWeightUpdateRequest request = this.remoteWeightUpdateWaitingOnGets.remove(requestNum);
            Weight existingWeight = this.distributedMap.getGetValue(response);
            Weight newWeight = request.getNewWeight();
            existingWeight.add(newWeight);
            this.backupPeerWeightedLink(new PeerWeightedLink(request.getUpdateRequestingPeerId(), existingWeight));
        }
        else if (this.pendingWeightRequests.containsKey(requestNum)) {
            ActorRef requester = this.pendingWeightRequests.remove(requestNum);
            UniversalId linkPeerId = this.distributedMap.getGetKey(response);
            Weight linkWeight = this.distributedMap.getGetValue(response);
            WeightResponse weightResponse = new WeightResponse(linkPeerId, linkWeight);
            requester.tell(weightResponse, getSelf());
        }
        else throw new RuntimeException();
    }
    
    /**
     * Extracts Peer ID from iteration response
     * Sends it back to the requester who requested this particular iteration as shown by the request number
     * @param response
     */
    protected void processIterationResponse(DistributedMapIterationResponse response) {
        int requestNum = response.getRequestNum();
        UniversalId linkPeerId = this.distributedMap.getIterationKey(response);
        ActorRef requester = this.pendingIterationRequests.get(requestNum);
        PeerLinkResponse peerLinkResponse = new PeerLinkResponse(linkPeerId);
        requester.tell(peerLinkResponse, getSelf());
    }
    
    /**
     * Asks the databaser to backup the peer weighted link with these current values
     * @param peerWeightedLink
     */
    protected void backupPeerWeightedLink(PeerWeightedLink peerWeightedLink) {
        BackupPeerLinkRequest request = new BackupPeerLinkRequest(peerWeightedLink);
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        databaser.tell(request, getSelf());
    }
    
    /**
     * Re-Add a backed up Peer Link at startup
     * @param response
     */
    protected void processBackedUpPeerLinkResponse(BackedUpPeerLinkResponse response) {
        PeerWeightedLink peerWeightedLink = response.getPeerWeightedLink();
        this.distributedMap.requestAdd(peerWeightedLink.getLinkedPeerId(), peerWeightedLink.getLinkWeight());
    }
}
