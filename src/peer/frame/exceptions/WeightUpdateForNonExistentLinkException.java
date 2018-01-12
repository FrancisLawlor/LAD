package peer.frame.exceptions;

import peer.frame.core.UniversalId;
import peer.graph.core.Weight;

/**
 * Exception thrown when attempting to update a weight of a non-existent peer graph link
 *
 */
public class WeightUpdateForNonExistentLinkException extends RuntimeException {
    private static final long serialVersionUID = -7174998124278065975L;
    
    public WeightUpdateForNonExistentLinkException(UniversalId linkPeerId, Weight linkWeight) {
        super("Weight of " + linkWeight.getWeight() + " attempted to be added to link with peer ID + " + linkPeerId.toString() + " that doesn't exist in this peer's graph");
    }
}
