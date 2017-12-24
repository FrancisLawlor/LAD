package peer.graph.weight;

import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

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
