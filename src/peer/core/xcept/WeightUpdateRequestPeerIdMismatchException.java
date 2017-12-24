package peer.core.xcept;

import peer.core.UniversalId;

/**
 * Exception if a peer tries to update its weighted linked incorrectly
 * Happens when sending a weight update request to the wrong Weighter Actor
 * The weighter actor is represented the weighted link with a different peer...
 * ... than the one you want to update your weight with
 *
 */
public class WeightUpdateRequestPeerIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = -3731747316945753326L;
    
    public WeightUpdateRequestPeerIdMismatchException(UniversalId peerId, UniversalId actualIdItShouldBe) {
        super("Attempting to update weight in a Weighter Actor that records a weighted link with " 
                 + actualIdItShouldBe.toString() + " rather than " +  peerId.toString());
    }
}
