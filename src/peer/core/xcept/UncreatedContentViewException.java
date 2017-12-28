package peer.core.xcept;

public class UncreatedContentViewException extends RuntimeException {
    private static final long serialVersionUID = 6246268871477733339L;
    
    public UncreatedContentViewException() {
        super("ViewingTime and Rating attempted to be recorded to a non existing Content View");
    }
}
