package peer.graph.weight;

import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

/**
 * Sends Update request to the other peer in the theoretical peer to peer link
 * Weights need to be kept consistent on both sides of the theoretical link
 *
 */
public class PeerWeightUpdateRequest extends RequestCommunication {
    private Weight newWeight;
    
    PeerWeightUpdateRequest(UniversalId originalRequester,UniversalId originalTarget, Weight newWeight){
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
