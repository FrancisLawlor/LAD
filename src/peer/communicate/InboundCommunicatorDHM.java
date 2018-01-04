package peer.communicate;

import akka.actor.ActorSelection;
import peer.core.ActorPaths;
import peer.core.xcept.RequestCommunicationTargetPeerIdMismatchException;
import peer.graph.weight.PeerWeightUpdateRequest;

/**
 * InboundCommunicator modified to send weight update requests to a PeerWeightedLinkorDHM
 *
 */
public class InboundCommunicatorDHM extends InboundCommunicator {
    /**
     * Will update weighted linked between this peer and the requesting peer if a link is recorded
     * @param request
     */
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest updateWeightRequest) {
        if (!updateWeightRequest.getOriginalTarget().equals(super.peerId)) 
            throw new RequestCommunicationTargetPeerIdMismatchException(updateWeightRequest.getOriginalTarget(), super.peerId);
        
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerWeightedLinkor.tell(updateWeightRequest, getSelf());
    }
}
