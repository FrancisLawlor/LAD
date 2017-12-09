package content.recommend.heuristic;

import content.recommend.PeerRecommendation;
import content.view.ViewHistoryResponse;

/**
 * Handles Heuristic for Recommendations
 *
 */
public interface HistoryHeuristic {
    static final int TOP_N = 10;
    
    PeerRecommendation getRecommendation(ViewHistoryResponse viewHistoryResponse);
}
