package content.view;

import content.recommend.PeerRecommendationRequest;
import core.ActorMessage;

public class ViewHistoryRequest extends ActorMessage {
    private PeerRecommendationRequest originalRequest;
    
    public ViewHistoryRequest(PeerRecommendationRequest originalRequest) {
        this.originalRequest = originalRequest;
    }
    
    public PeerRecommendationRequest getOriginalRequest() {
        return this.originalRequest;
    }
}
