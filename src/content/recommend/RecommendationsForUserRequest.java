package content.recommend;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;

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
