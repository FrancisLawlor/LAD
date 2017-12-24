package peer.core.xcept;

import peer.core.UniversalId;

/**
 * Exception thrown when asking the wrong weighter for the weight of a linked peer
 *
 */
public class WeightRequestPeerIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = 4989429899420142098L;
    
    public WeightRequestPeerIdMismatchException(UniversalId peerId, UniversalId actualIdItShouldBe) {
        super("Weighter represents weight of link with " 
                + actualIdItShouldBe.toString() + " not " + peerId.toString());
    }
}
