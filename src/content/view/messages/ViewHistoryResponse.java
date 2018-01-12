package content.view.messages;

import content.recommend.messages.PeerRecommendationRequest;
import content.view.core.ViewHistory;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

public class ViewHistoryResponse extends ActorMessage {
    private ViewHistory history;
    private PeerRecommendationRequest originalRequest;
    
    public ViewHistoryResponse(ViewHistory history, ViewHistoryRequest request) {
        super(ActorMessageType.ViewHistoryResponse);
        this.history = history;
        this.originalRequest = request.getOriginalRequest();
    }
    
    public ViewHistory getViewHistory() {
        return this.history;
    }
    
    public PeerRecommendationRequest getOriginalRequest() {
        return this.originalRequest;
    }
}
