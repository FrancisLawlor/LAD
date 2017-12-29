package content.recommend.heuristic;

import java.util.LinkedList;
import java.util.List;

import content.recommend.PeerRecommendation;
import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.WeightedPeerRecommendation;

/**
 * Deterministic Heuristic to be used for testing
 * Returns aggregated content deterministically from weighted peer recommendations
 *
 */
public class DeterministicAggregationHeuristic extends AggregationHeuristic {
    /**
     * Recommendations from a PeerRecommendation enter into the recommendationsForUser according to peer weight
     * Peer Weight determines the proportion of the list size this peer's recommendations will take up
     * Peer weight normalised by total weight of all peers
     */
    public RecommendationsForUser getRecommendationsForUser(List<WeightedPeerRecommendation> peerRecommends) {
        List<Recommendation> recommendationList = new LinkedList<Recommendation>();
        
        double totalWeight = super.getTotalWeight(peerRecommends.iterator());
        for (WeightedPeerRecommendation weightedPeerRecommend : peerRecommends) {
            double weight = weightedPeerRecommend.getWeight();
            double proportionOfEntries = weight / totalWeight;
            int numEntries = (int)(LIMIT * proportionOfEntries);
            PeerRecommendation peerRecommendation = weightedPeerRecommend.getPeerRecommendation();
            for (int i = 0; i < numEntries && i < peerRecommendation.size() && recommendationList.size() < LIMIT; i++) {
                Recommendation recommendation = peerRecommendation.getRecommendationAtRank(i);
                recommendationList.add(recommendation);
            }
        }
        
        RecommendationsForUser recommendationsForUser = new RecommendationsForUser(recommendationList);
        return recommendationsForUser;
    }
}
