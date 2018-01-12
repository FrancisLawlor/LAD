package peer.frame.exceptions;

public class UnrecordedContentViewException extends RuntimeException {
    private static final long serialVersionUID = 5908160055153416037L;
    
    public UnrecordedContentViewException() {
        super("ContentView has not been recorded before another one was attempted to be created");
    }
}
