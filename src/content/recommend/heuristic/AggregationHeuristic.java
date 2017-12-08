package content.recommend.heuristic;

import java.util.Iterator;

import content.recommend.RecommendationsForUser;
import content.recommend.WeightedPeerRecommendation;

public interface AggregationHeuristic {
    RecommendationsForUser getRecommendationsForUser(
            Iterator<WeightedPeerRecommendation> weightedPeerRecommendations);
}
