package core.xcept;

public class WrongPeerIdException extends RuntimeException {
    private static final long serialVersionUID = -6222170757936689434L;
    
    public WrongPeerIdException() {
        super("Request has been routed to wrong Peer Id!");
    }
}
