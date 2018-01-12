package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;

/**
 * Actor Message that requests a Weighter's Weight
 * Requests weight of a theoretical link between the local user and a remote peer, 
 * which the Weighter (Weight Actor) represents
 *
 */
public class WeightRequest extends ActorMessage {
    private UniversalId linkedPeerId;
    
    public WeightRequest(UniversalId linkedPeerId) {
        super(ActorMessageType.WeightRequest);
        this.linkedPeerId = linkedPeerId;
    }
    
    public UniversalId getLinkedPeerId() {
        return this.linkedPeerId;
    }
}
