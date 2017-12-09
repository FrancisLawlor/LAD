package core.xcept;

public class UnknownMessageException extends RuntimeException {
    private static final long serialVersionUID = -2601642573950644996L;
    
    public UnknownMessageException() {
        super("Unrecognised Message; Debug");
    }
}
