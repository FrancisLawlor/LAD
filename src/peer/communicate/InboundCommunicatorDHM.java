package peer.communicate;

import akka.actor.ActorSelection;
import peer.core.ActorPaths;
import peer.core.xcept.PeerToPeerRequestTargetPeerIdMismatchException;
import peer.graph.distributedmap.RemotePeerWeightedLinkAddition;
import peer.graph.weight.PeerWeightUpdateRequest;

/**
 * InboundCommunicator modified to send weight update requests to a PeerWeightedLinkorDHM
 *
 */
public class InboundCommunicatorDHM extends InboundCommunicator {
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof RemotePeerWeightedLinkAddition) {
            RemotePeerWeightedLinkAddition addition = (RemotePeerWeightedLinkAddition) message;
            this.processRemotePeerWeightedLinkAddition(addition);
        }
        else {
            super.onReceive(message);
        }
    }
    
    /**
     * Will tell the PeerWeightedLinkorDHM to add a weighted link between this peer and the requesting peer
     * @param addition
     */
    protected void processRemotePeerWeightedLinkAddition(RemotePeerWeightedLinkAddition addition) {
        if (!addition.getOriginalTarget().equals(super.peerId)) 
            throw new PeerToPeerRequestTargetPeerIdMismatchException(addition.getOriginalTarget(), super.peerId);
        
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerWeightedLinkor.tell(addition, getSelf());
    }
    
    /**
     * Will tell the PeerWeightedLinkorDHM to update weighted linked between this peer and the requesting peer if a link is recorded
     * @param request
     */
    @Override
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest updateWeightRequest) {
        if (!updateWeightRequest.getOriginalTarget().equals(super.peerId)) 
            throw new PeerToPeerRequestTargetPeerIdMismatchException(updateWeightRequest.getOriginalTarget(), super.peerId);
        
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerWeightedLinkor.tell(updateWeightRequest, getSelf());
    }
}
