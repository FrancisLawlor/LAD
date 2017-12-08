package content.recommend.heuristic;

import content.recommend.PeerRecommendation;
import content.view.ViewHistoryResponse;

/**
 * Handles Heuristic for Recommendations
 *
 */
public interface HistoryHeuristic {
    PeerRecommendation getRecommendation(ViewHistoryResponse viewHistoryResponse);
}
