package content.recommend.heuristic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import content.core.Content;
import content.view.ContentView;
import content.view.ViewHistory;
import content.view.ViewHistoryResponse;

/**
 * Weighted Probability Heuristic
 * Returns the Top N recommendations from this peer's view history
 * Inserts Recommendations into list by weighted probability
 *
 */
public class WeightedProbabilityHistoryHeuristic implements HistoryHeuristic {    
    /**
     * Gets Recommendation of the Top N viewed Content from View History
     */
    public List<Content> getRecommendation(ViewHistoryResponse viewHistoryResponse) {
        List<Content> contentList = new LinkedList<Content>();
        
        ViewHistory viewHistory = viewHistoryResponse.getViewHistory();
        
        double totalScore = this.getTotalScore(viewHistory.iterator());
        Iterator<ContentView> views = viewHistory.iterator();
        while (views.hasNext()) {
            ContentView view = views.next();
            if (contentList.size() < TOP_N) {
                double myScore = view.getScore();
                double weightedChanceOfEntry = myScore / totalScore;
                double threshold = ThreadLocalRandom.current().nextDouble(1.0);
                if (weightedChanceOfEntry > threshold) {
                    contentList.add(view.getContent());
                }
            }
        }
        return contentList;
    }
    
    /**
     * Get Total Score for normalisation of a score
     * @param contentViews
     * @return
     */
    private double getTotalScore(Iterator<ContentView> contentViews) {
        double totalScore = 0.0;
        while (contentViews.hasNext()) {
            ContentView contentView = contentViews.next();
            totalScore += contentView.getScore();
        }
        return totalScore;
    }
}
