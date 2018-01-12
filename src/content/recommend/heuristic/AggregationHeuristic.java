package content.recommend.heuristic;

import java.util.Iterator;
import java.util.List;

import content.recommend.core.WeightedPeerRecommendation;
import content.recommend.messages.RecommendationsForUser;

/**
 * Abstract superclass for Aggregation Heuristics
 *
 */
public abstract class AggregationHeuristic {
    static final int LIMIT = 10;
    
    /**
     * Subclass must implements the getting of recommendations for the user from weighted lists of peer recomemndations with their particular heuristic
     * @param peerRecommends
     * @return
     */
    public abstract RecommendationsForUser getRecommendationsForUser(List<WeightedPeerRecommendation> peerRecommends);
    
    /**
     * Get Total Weight for normalisation of a weight
     * @param weightedPeerRecommendations
     * @return
     */
    protected double getTotalWeight(Iterator<WeightedPeerRecommendation> weightedPeerRecommendations) {
        double totalWeight = 0.0;
        while (weightedPeerRecommendations.hasNext()) {
            WeightedPeerRecommendation weightedPeerRecommendation = weightedPeerRecommendations.next();
            totalWeight += weightedPeerRecommendation.getWeight();
        }
        return totalWeight;
    }
}
