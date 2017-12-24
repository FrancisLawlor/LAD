package content.recommend;

import java.util.Iterator;
import java.util.List;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Contains Curated Aggregated Content Recommendations from other peers
 * To be passed back to the User of this peer node through the Viewer actor
 *
 */
public class RecommendationsForUser extends ActorMessage implements Iterable<Recommendation> {
    private List<Recommendation> recommendations;
    
    public RecommendationsForUser(List<Recommendation> recommendations) {
        super(ActorMessageType.PeerRecommendationsForUser);
        this.recommendations = recommendations;
    }
    
    public Iterator<Recommendation> iterator() {
        return this.recommendations.iterator();
    }
}
