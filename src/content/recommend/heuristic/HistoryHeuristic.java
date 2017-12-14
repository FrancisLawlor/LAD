package content.recommend.heuristic;

import java.util.List;

import content.content.Content;
import content.view.ViewHistoryResponse;

/**
 * Handles Heuristic for Recommendations
 *
 */
public interface HistoryHeuristic {
    static final int TOP_N = 10;
    
    List<Content> getRecommendation(ViewHistoryResponse viewHistoryResponse);
}
