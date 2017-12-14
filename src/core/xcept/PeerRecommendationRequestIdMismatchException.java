package core.xcept;

/**
 * Exception thrown when using the same temporary delegated actor...
 * ... for two different Peer Recommendation generation requests
 * HistoryRecommendationGenerator should only be delegated to for a unique peer's recommendation generation
 *
 */
public class PeerRecommendationRequestIdMismatchException extends RuntimeException {
    private static final long serialVersionUID = 4068680044764724566L;
    
    public PeerRecommendationRequestIdMismatchException() {
        super("Attempting to request Recommendations for a peer from a HistoryRecommendationGenerator that was spawned to generate for another peer\n" +
                "Create another HistoryRecommendationGenerator to generate recommendations for the new requesting peer!");
    }

}