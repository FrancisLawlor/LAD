package peer.graph.core;

import peer.frame.core.UniversalId;

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
