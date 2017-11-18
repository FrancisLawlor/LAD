package content.view;

import content.recommend.PeerRecommendationRequest;
import core.ActorMessage;

public class ViewHistoryResponse extends ActorMessage {
    private ViewHistory history;
    private PeerRecommendationRequest originalRequest;
    
    public ViewHistoryResponse(ViewHistory history, ViewHistoryRequest request) {
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
