package content.recommend.heuristic;

import java.util.List;

import content.frame.core.Content;
import content.view.messages.ViewHistoryResponse;

/**
 * Handles Heuristic for Recommendations
 *
 */
public interface HistoryHeuristic {
    static final int TOP_N = 10;
    
    List<Content> getRecommendation(ViewHistoryResponse viewHistoryResponse);
}
