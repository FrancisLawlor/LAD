package peer.core.xcept;

import peer.core.UniversalId;

/**
 * Exception thrown when RequestCommunication subclass has wrong target peer ID
 *
 */
public class PeerToPeerRequestTargetPeerIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = 9045004325696742946L;
    
    public PeerToPeerRequestTargetPeerIdMismatchException(UniversalId peerId, UniversalId actualIdItShouldBe) {
        super("PeerToPeerRequest subclass has wrong target peer ID: " 
                + peerId.toString() + " instead of " + actualIdItShouldBe.toString());
    }
}
