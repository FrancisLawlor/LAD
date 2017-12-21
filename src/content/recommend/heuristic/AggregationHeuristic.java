package content.recommend.heuristic;

import java.util.List;

import content.recommend.RecommendationsForUser;
import content.recommend.WeightedPeerRecommendation;

public interface AggregationHeuristic {
    static final int LIMIT = 10;
    
    RecommendationsForUser getRecommendationsForUser(List<WeightedPeerRecommendation> peerRecommends);
}
