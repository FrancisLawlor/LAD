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
        double totalScore = getTotalScore(viewHistory.iterator());
        
        List<ContentView> temp = getDepletableList(viewHistory.iterator());
        
        for (int i = 0; i < TOP_N && contentList.size() < TOP_N && temp.size() > 0; i++) {
            int j = 0;
            while (j < temp.size() && contentList.size() < TOP_N) {
                ContentView view = temp.get(j);
                double myScore = view.getScore();
                double weightedChanceOfEntry = myScore / totalScore;
                double threshold = ThreadLocalRandom.current().nextDouble(1.0);
                if (weightedChanceOfEntry > threshold) {
                    contentList.add(view.getContent());
                    temp.remove(j);
                }
                else {
                    j++;
                }
            }
        }
        return contentList;
    }
    
    /**
     * Gets a List of Content Views which is depletable
     * Content Views will be removed from it without replacement
     * @param contentViews
     * @return
     */
    private static List<ContentView> getDepletableList(Iterator<ContentView> contentViews) {
        List<ContentView> depletableList = new LinkedList<ContentView>();
        while (contentViews.hasNext()) {
            depletableList.add(contentViews.next());
        }
        return depletableList;
    }
    
    /**
     * Get Total Score for normalisation of a score
     * @param contentViews
     * @return
     */
    private static double getTotalScore(Iterator<ContentView> contentViews) {
        double totalScore = 0.0;
        while (contentViews.hasNext()) {
            ContentView contentView = contentViews.next();
            totalScore += contentView.getScore();
        }
        return totalScore;
    }
}
