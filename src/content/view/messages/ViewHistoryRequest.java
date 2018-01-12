package content.view.messages;

import content.recommend.messages.PeerRecommendationRequest;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

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
