package peer.graph.weight;

import core.ActorMessage;
import core.ActorMessageType;
import core.UniversalId;

/**
 * Sent locally after content processing to change peer to peer link weight
 *
 */
public class LocalWeightUpdateRequest extends ActorMessage {
    private UniversalId linkedPeerId;
    private Weight newWeight;
    
    public LocalWeightUpdateRequest(UniversalId linkedPeerId, Weight newWeight){
        super(ActorMessageType.LocalWeightUpdateRequest);
        this.linkedPeerId = linkedPeerId;
        this.newWeight = newWeight;
    }
    
    public UniversalId getLinkedPeerId() {
        return this.linkedPeerId;
    }
    
    public Weight getNewWeight() {
        return this.newWeight;
    }
}
