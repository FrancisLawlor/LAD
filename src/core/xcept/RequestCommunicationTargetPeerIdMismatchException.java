package core.xcept;

import core.UniversalId;

/**
 * Exception thrown when RequestCommunication subclass has wrong target peer ID
 *
 */
public class RequestCommunicationTargetPeerIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = 9045004325696742946L;
    
    public RequestCommunicationTargetPeerIdMismatchException(UniversalId peerId, UniversalId actualIdItShouldBe) {
        super("RequestCommunication subclass has wrong target peer ID: " 
                + peerId.toString() + " instead of " + actualIdItShouldBe.toString());
    }
}
