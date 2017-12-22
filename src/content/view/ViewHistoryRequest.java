package content.view;

import content.recommend.PeerRecommendationRequest;
import core.ActorMessage;
import core.ActorMessageType;

public class ViewHistoryRequest extends ActorMessage {
    private PeerRecommendationRequest originalRequest;
    
    public ViewHistoryRequest(PeerRecommendationRequest originalRequest) {
        super(ActorMessageType.ViewHistoryRequest);
        this.originalRequest = originalRequest;
    }
    
    public PeerRecommendationRequest getOriginalRequest() {
        return this.originalRequest;
    }
}
