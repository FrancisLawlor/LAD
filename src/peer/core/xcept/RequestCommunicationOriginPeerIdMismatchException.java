package peer.core.xcept;

import peer.core.UniversalId;

/**
 * Exception thrown when RequestCommunication subclass has wrong originating requester peer ID
 *
 */
public class RequestCommunicationOriginPeerIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = -1445446019887072778L;
    
    public RequestCommunicationOriginPeerIdMismatchException(UniversalId peerId, UniversalId actualIdItShouldBe) {
        super("RequestCommunication subclass has wrong originating requester peer ID: " 
                + peerId.toString() + " instead of " + actualIdItShouldBe.toString());
    }
}
