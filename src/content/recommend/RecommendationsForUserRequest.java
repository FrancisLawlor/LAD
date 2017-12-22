package content.recommend;

import core.ActorMessage;
import core.ActorMessageType;
import core.UniversalId;

/**
 * Requests Recommendations from a Recommender for a Viewer
 *
 */
public class RecommendationsForUserRequest extends ActorMessage {
    private UniversalId userPeerId;
    
    public RecommendationsForUserRequest(UniversalId userPeerId) {
        super(ActorMessageType.PeerRecommendationForUserRequest);
        this.userPeerId = userPeerId;
    }
    
    public UniversalId getUserPeerId() {
        return this.userPeerId;
    }
}
