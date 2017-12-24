package peer.graph.weight;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;

/**
 * Actor Message that carries the Weighter's Weight
 * Sends back the weight of a theoretical link between the local user and a remote peer, 
 * which the Weighter (Weight Actor) represents
 *
 */
public class WeightResponse extends ActorMessage {
    private UniversalId linkedPeerId;
    private Weight linkWeight;
    
    public WeightResponse(UniversalId linkedPeerId, Weight linkWeight) {
        super(ActorMessageType.WeightResponse);
        this.linkedPeerId = linkedPeerId;
        this.linkWeight = linkWeight;
    }
    
    public UniversalId getLinkedPeerId() {
        return this.linkedPeerId;
    }
    
    public Weight getLinkWeight() {
        return this.linkWeight;
    }
}
