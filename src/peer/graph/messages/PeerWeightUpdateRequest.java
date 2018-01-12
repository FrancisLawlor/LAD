package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerRequest;
import peer.graph.core.Weight;

/**
 * Sends Update request to the other peer in the theoretical peer to peer link
 * Weights need to be kept consistent on both sides of the theoretical link
 *
 */
public class PeerWeightUpdateRequest extends PeerToPeerRequest {
    private Weight newWeight;
    
    public PeerWeightUpdateRequest(UniversalId originalRequester,UniversalId originalTarget, Weight newWeight){
        super(ActorMessageType.PeerWeightUpdateRequest, originalRequester, originalTarget);
        this.newWeight = newWeight;
    }
    
    public UniversalId getUpdateRequestingPeerId() {
        return super.getOriginalRequester();
    }
    
    public UniversalId getTargetPeerId() {
        return super.getOriginalTarget();
    }
    
    public Weight getNewWeight() {
        return this.newWeight;
    }
}
