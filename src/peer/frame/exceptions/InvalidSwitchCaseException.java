package peer.frame.exceptions;

public class InvalidSwitchCaseException extends RuntimeException {
    private static final long serialVersionUID = 8800745415276410335L;
    
    public InvalidSwitchCaseException() {
        super("Invalid Switch Case");
    }
}
