package content.recommend.heuristic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
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
        
        double maxScore = this.getMaxScore(viewHistory.iterator());
        Iterator<ContentView> views = viewHistory.iterator();
        while (views.hasNext()) {
            ContentView view = views.next();
            if (contentList.size() < TOP_N) {
                double myScore = view.getScore();
                double weightedChanceOfEntry = myScore / maxScore;
                double threshold = ThreadLocalRandom.current().nextDouble(1.0);
                if (weightedChanceOfEntry > threshold) {
                    contentList.add(view.getContent());
                }
            }
        }
        return contentList;
    }
    
    /**
     * Helper to get max score of top scored content
     * Helps express scores in relative terms to the max
     * Used to generate probability between 0 and 1 for content to enter list
     * @param views
     * @return
     */
    private double getMaxScore(Iterator<ContentView> views) {
        double maxScore;
        Stack<ContentView> maxFinder = new Stack<ContentView>();
        while (views.hasNext()) {
            ContentView view = views.next();
            if (maxFinder.isEmpty()) {
                maxFinder.push(view);
            }
            else if (view.getScore() > maxFinder.peek().getScore()){
                maxFinder.pop();
                maxFinder.push(view);
            }
        }
        maxScore = maxFinder.pop().getScore();
        return maxScore;
    }
}
