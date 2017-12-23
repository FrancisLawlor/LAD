package peer.graph.weight;

import core.ActorMessage;
import core.ActorMessageType;
import core.UniversalId;

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
