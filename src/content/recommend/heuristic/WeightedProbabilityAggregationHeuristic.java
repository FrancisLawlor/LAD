package content.recommend.heuristic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.WeightedPeerRecommendation;

/**
 * Weighted Probabilistic Heuristic for Peer Recommendation Content Aggregation
 * returns weighted content from peer recommendations in a probablistic fashion
 *
 */
public class WeightedProbabilityAggregationHeuristic implements AggregationHeuristic {
    /**
     * Sorts Peer Recommendations by Weight
     * Adds Content from Peer Recommendations to Content List probabilistically based on weight
     * Uses weight to weigh each Peer Recommendation relative to the max weight overall
     * Uses this relative weight as a chance of entry into the list for content in the peer recommendation
     * Cycles through the content in each peer recommendation and then...
     * ...Checks if the chance of entry exceeds a random threshold
     * @return recommendationsForUser generated from contentList
     */
    public RecommendationsForUser getRecommendationsForUser(List<WeightedPeerRecommendation> peerRecommends) {
        double totalWeight = this.getTotalWeight(peerRecommends.iterator());
        List<Recommendation> recommendationList = new LinkedList<Recommendation>();
        for (WeightedPeerRecommendation weightedPeerRecommendations : peerRecommends) {
            if (recommendationList.size() < LIMIT) {
                double weight = weightedPeerRecommendations.getWeight();
                double weightedChanceOfEntry = weight / totalWeight;
                for (Recommendation recommendation : weightedPeerRecommendations.getPeerRecommendation()) {
                    double threshold = ThreadLocalRandom.current().nextDouble(1.0);
                    if (weightedChanceOfEntry > threshold) {
                        recommendationList.add(recommendation);
                    }
                }
            }
        }
        
        RecommendationsForUser recommendationsForUser = new RecommendationsForUser(recommendationList);
        return recommendationsForUser;
    }
    
    /**
     * Get Total Weight for normalisation of a weight
     * @param weightedPeerRecommendations
     * @return
     */
    private double getTotalWeight(Iterator<WeightedPeerRecommendation> weightedPeerRecommendations) {
        double totalWeight = 0.0;
        while (weightedPeerRecommendations.hasNext()) {
            WeightedPeerRecommendation weightedPeerRecommendation = weightedPeerRecommendations.next();
            totalWeight += weightedPeerRecommendation.getWeight();
        }
        return totalWeight;
    }
}
