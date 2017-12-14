package peer.graph.weight;

import core.ActorMessage;
import core.UniversalId;

/**
 * Initialises Weighter with peer to which it represents a weighted link
 * Initialises Weighter with weight of the theoretical link to this peer
 *
 */
public class WeighterInit extends ActorMessage {
    private UniversalId linkedPeerId;
    private Weight initialLinkWeight;
    
    public WeighterInit(UniversalId linkedPeerId, Weight initialLinkWeight) {
        this.linkedPeerId = linkedPeerId;
        this.initialLinkWeight = initialLinkWeight;
    }
    
    public UniversalId getLinkedPeerId() {
        return this.linkedPeerId;
    }
    
    public Weight getInitialLinkWeight() {
        return this.initialLinkWeight;
    }
}
