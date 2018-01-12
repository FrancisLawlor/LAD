package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;
import peer.graph.core.Weight;

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
