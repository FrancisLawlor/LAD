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
public class WeightedProbabilityAggregationHeuristic extends AggregationHeuristic {
    /**
     * Temporary inner class for storing each individual peer recommendation with peer weight
     *
     */
    private class WeightedRecommendation {
        private Recommendation recommendation;
        private double weight;
        
        public WeightedRecommendation(Recommendation recommendation, double weight) {
            this.recommendation = recommendation;
            this.weight = weight;
        }
    }
    /**
     * Sorts Peer Recommendations by Weight
     * Adds Content from Peer Recommendations to Content List probabilistically based on weight
     * Uses weight to weigh each Peer Recommendation relative to the total weight of all weighted peer recommendations
     * Uses this normalised weight as a chance of entry into the list for content in the peer recommendation
     * Cycles through the content in each peer recommendation and then...
     * ...Checks if the chance of entry exceeds a random threshold
     * @return recommendationsForUser generated from contentList
     */
    public RecommendationsForUser getRecommendationsForUser(List<WeightedPeerRecommendation> peerRecommends) {
        List<Recommendation> recommendationList = new LinkedList<Recommendation>();
        
        List<WeightedRecommendation> temp = this.getDepletableListOfWeightedRecommendations(peerRecommends.iterator());
        
        double totalWeight = this.getTotalWeight(peerRecommends.iterator());
        for (int i = 0; i < LIMIT && recommendationList.size() < LIMIT && temp.size() > 0; i++) {
            int j = 0;
            while (j < temp.size() && recommendationList.size() < LIMIT) {
                WeightedRecommendation weightedRecommendation = temp.get(j);
                double weight = weightedRecommendation.weight;
                double weightedChanceOfEntry = weight / totalWeight;
                double threshold = ThreadLocalRandom.current().nextDouble(1.0);
                if (weightedChanceOfEntry > threshold) {
                    recommendationList.add(weightedRecommendation.recommendation);
                    temp.remove(j);
                }
                else {
                    j++;
                }
            }
        }
        
        RecommendationsForUser recommendationsForUser = new RecommendationsForUser(recommendationList);
        return recommendationsForUser;
    }
    
    /**
     * Gets a list of each recommendation individually weighted
     * List is depletable and weighted recommendations are removed without replacement
     * @param weightedPeerRecommendations
     * @return
     */
    private List<WeightedRecommendation> getDepletableListOfWeightedRecommendations(Iterator<WeightedPeerRecommendation> weightedPeerRecommendations) {
        List<WeightedRecommendation> depletableList = new LinkedList<WeightedRecommendation>();
        while (weightedPeerRecommendations.hasNext()) {
            WeightedPeerRecommendation weightedPeerRecommendation = weightedPeerRecommendations.next();
            double weight = weightedPeerRecommendation.getWeight();
            for (Recommendation recommendation : weightedPeerRecommendation.getPeerRecommendation()) {
                depletableList.add(new WeightedRecommendation(recommendation, weight));
            }
        }
        return depletableList;
    }
}
