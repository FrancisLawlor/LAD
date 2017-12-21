package core.xcept;

/**
 * Exception thrown when asking the wrong weighter for the weight of a linked peer
 *
 */
public class WeightRequestPeerIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = 4989429899420142098L;
    
    public WeightRequestPeerIdMismatchException() {
        super("Weighter represents weight of link with a different peer!");
    }
}
