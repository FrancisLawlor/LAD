package core.xcept;

/**
 * Exception thrown when RequestCommunication subclass has wrong originating requester peer ID
 *
 */
public class RequestCommunicationOriginPeerIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = -1445446019887072778L;
    
    public RequestCommunicationOriginPeerIdMismatchException() {
        super("RequestCommunication subclass has wrong originating requester peer ID!");
    }
}
