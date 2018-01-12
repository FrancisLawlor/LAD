package peer.frame.exceptions;

public class InitialisationException extends RuntimeException {
    private static final long serialVersionUID = 6137401507843655018L;
    
    public InitialisationException() {
        super("Trying to initialise PeerToPeerActor more than once!");
    }
}
