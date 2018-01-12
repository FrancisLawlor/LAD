package content.recommend.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;

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
