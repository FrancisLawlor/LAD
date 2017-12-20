package content.recommend.heuristic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import content.Content;
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
    public RecommendationsForUser getRecommendationsForUser(
            Iterator<WeightedPeerRecommendation> weightedPeerRecommendations) {
        List<WeightedPeerRecommendation> recommendationList = 
                new LinkedList<WeightedPeerRecommendation>();
        
        while (weightedPeerRecommendations.hasNext()) {
            WeightedPeerRecommendation recommendation = weightedPeerRecommendations.next();
            recommendationList.add(recommendation);
        }
        
        double maxWeight = this.getMaxWeight(recommendationList.iterator());
        List<Content> contentList = new LinkedList<Content>();
        for (WeightedPeerRecommendation recommendation : recommendationList) {
            if (contentList.size() < LIMIT) {
                double weight = recommendation.getWeight();
                double weightedChanceOfEntry = weight / maxWeight;
                for (Content content : recommendation.getPeerRecommendation()) {
                    double threshold = ThreadLocalRandom.current().nextDouble(1.0);
                    if (weightedChanceOfEntry > threshold) {
                        contentList.add(content);
                    }
                }
            }
        }
        
        RecommendationsForUser recommendationsForUser = new RecommendationsForUser(contentList);
        return recommendationsForUser;
    }
    
    /**
     * Helper to get max weight of peer recommendations
     * Helps express peer recommendations in relative terms to the max
     * Used to generate probability between 0 and 1 for peer recommendations
     * @param weightedRecommends
     * @return
     */
    private double getMaxWeight(Iterator<WeightedPeerRecommendation> weightedRecommends) {
        double maxScore;
        Stack<WeightedPeerRecommendation> maxFinder = new Stack<WeightedPeerRecommendation>();
        while (weightedRecommends.hasNext()) {
            WeightedPeerRecommendation weightedRecommendation = weightedRecommends.next();
            if (maxFinder.isEmpty()) {
                maxFinder.push(weightedRecommendation);
            }
            else if (weightedRecommendation.getWeight() > maxFinder.peek().getWeight()){
                maxFinder.pop();
                maxFinder.push(weightedRecommendation);
            }
        }
        maxScore = maxFinder.pop().getWeight();
        return maxScore;
    }
}
