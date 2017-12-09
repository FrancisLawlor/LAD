package content.recommend;

import java.util.Iterator;
import java.util.List;

import content.content.Content;
import core.ActorMessage;

/**
 * Contains Curated Aggregated Content Recommendations from other peers
 * To be passed back to the User of this peer node through the Viewer actor
 *
 */
public class RecommendationsForUser extends ActorMessage implements Iterable<Content> {
    private List<Content> recommendations;
    
    public RecommendationsForUser(List<Content> recommendations) {
        this.recommendations = recommendations;
    }
    
    public Iterator<Content> iterator() {
        return this.recommendations.iterator();
    }
}
