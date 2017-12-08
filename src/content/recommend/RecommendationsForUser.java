package content.recommend;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import core.ActorMessage;
import core.Recommendation;

public class RecommendationsForUser extends ActorMessage implements Iterable<Recommendation> {
    private List<Recommendation> recommendations;
    
    public RecommendationsForUser() {
        this.recommendations = new LinkedList<Recommendation>();
    }
    
    public Iterator<Recommendation> iterator() {
        return this.recommendations.iterator();
    }
}
