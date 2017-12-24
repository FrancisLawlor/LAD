package content.recommend;

import java.util.Iterator;
import java.util.List;

import content.core.Content;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Contains Curated Aggregated Content Recommendations from other peers
 * To be passed back to the User of this peer node through the Viewer actor
 *
 */
public class RecommendationsForUser extends ActorMessage implements Iterable<Content> {
    private List<Content> recommendations;
    
    public RecommendationsForUser(List<Content> recommendations) {
        super(ActorMessageType.PeerRecommendationsForUser);
        this.recommendations = recommendations;
    }
    
    public Iterator<Content> iterator() {
        return this.recommendations.iterator();
    }
}
