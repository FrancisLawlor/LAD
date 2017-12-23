package peer.graph.weight;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import core.ActorPaths;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import core.xcept.UnknownMessageException;
import core.xcept.WeightRequestPeerIdMismatchException;
import core.xcept.WeightUpdateRequestPeerIdMismatchException;
import core.xcept.WrongPeerIdException;

/**
 * Represents a theoretical weight of a link between the local user and a remote peer
 * Handles weight update requests
 * Keeps link weight consistent for both peers
 *
 */
public class Weighter extends PeerToPeerActor {
    private UniversalId linkedPeerId;
    private Weight linkWeight;
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof WeighterInit) {
            WeighterInit init = (WeighterInit) message;
            this.linkedPeerId = init.getLinkedPeerId();
            this.linkWeight = init.getInitialLinkWeight();
        }
        else if (message instanceof WeightRequest) {
            WeightRequest weightRequest = 
                    (WeightRequest) message;
            this.processWeightRequest(weightRequest);
        }
        else if (message instanceof LocalWeightUpdateRequest) {
            LocalWeightUpdateRequest weightUpdateRequest = 
                    (LocalWeightUpdateRequest) message;
            this.processLocalWeightUpdateRequest(weightUpdateRequest);
        }
        else if (message instanceof PeerWeightUpdateRequest) {
            PeerWeightUpdateRequest weightUpdateRequest = 
                    (PeerWeightUpdateRequest) message;
            this.processPeerWeightUpdateRequest(weightUpdateRequest);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Sends back the weight of the theoretical edge/link between users
     * @param weightRequest
     */
    protected void processWeightRequest(WeightRequest weightRequest) {
        if (weightRequest.getPeerId().equals(this.linkedPeerId)) {
            WeightResponse weightResponse = new WeightResponse(linkedPeerId, this.linkWeight);
            ActorRef requester = getSender();
            requester.tell(weightResponse, getSelf());
        }
        else throw new WeightRequestPeerIdMismatchException(weightRequest.getPeerId(), this.linkedPeerId);
    }
    
    /**
     * Updates weight from local request
     * Sends a request to the other peer that is theoretically linked by this weight to this peer
     * Asks the other peer to update its weight to keep the link weights consistent on both sides
     * @param updateRequest
     */
    protected void processLocalWeightUpdateRequest(LocalWeightUpdateRequest updateRequest) {
        if (!this.linkedPeerId.equals(updateRequest.getLinkedPeerId())) 
            throw new WeightUpdateRequestPeerIdMismatchException(updateRequest.getLinkedPeerId(), this.linkedPeerId);
        
        Weight weight = updateRequest.getNewWeight();
        this.linkWeight = weight;
        
        PeerWeightUpdateRequest request = new PeerWeightUpdateRequest(super.peerId, this.linkedPeerId, weight);
        ActorSelection communicator = getContext().actorSelection(ActorPaths.getPathToOutComm());
        communicator.tell(request, getSelf());
    }
    
    /**
     * Updates weight due to peer request
     * Request should come from peer on other side of theoretical weighted link
     * Maintains consistent link weight in both frames of reference for both linked peers
     * @param updateRequest
     */
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest updateRequest) {
        if (super.peerId.equals(updateRequest.getTargetPeerId())) {
            if (this.linkedPeerId.equals(updateRequest.getUpdateRequestingPeerId())) {
                this.linkWeight = updateRequest.getNewWeight();
            }
            else throw new WeightUpdateRequestPeerIdMismatchException(updateRequest.getUpdateRequestingPeerId(), this.linkedPeerId);
        }
        else throw new WrongPeerIdException(updateRequest.getTargetPeerId(), super.peerId);
    }
}
