package peer.graph.link;

import core.UniversalId;
import peer.graph.weight.Weight;

/**
 * Signals a Link needs to be recorded to this Peer ID
 *
 */
public class PeerLinkAddition {
    private UniversalId peerId;
    private Weight startingWeight;
    
    public PeerLinkAddition(UniversalId peerId, Weight startingWeight) {
        this.peerId = peerId;
    }
    
    public UniversalId getPeerId() {
        return this.peerId;
    }
    
    public Weight getStartingWeight() {
        return this.startingWeight;
    }
}
