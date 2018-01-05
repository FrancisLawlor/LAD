package peer.graph.distributedmap;

import peer.core.UniversalId;
import peer.graph.weight.Weight;

/**
 * Encapsulates Peer Weighted Link information
 *
 */
public class PeerWeightedLink {
    private UniversalId linkedPeerId;
    private Weight linkWeight;
    
    public PeerWeightedLink(UniversalId linkedPeerId, Weight linkWeight) {
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
