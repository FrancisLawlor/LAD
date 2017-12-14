package content.recommend;

import core.ActorMessage;
import core.UniversalId;

public class RecommendationsForUserRequest extends ActorMessage {
    private UniversalId userPeerId;
    
    public RecommendationsForUserRequest(UniversalId userPeerId) {
        this.userPeerId = userPeerId;
    }
    
    public UniversalId getUserPeerId() {
        return this.userPeerId;
    }
}
