package content.recommend;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import content.view.ViewHistory;
import content.view.ViewHistoryRequest;
import content.view.ViewHistoryResponse;

/**
 * Generates Recommendation from this peer based on View History
 *
 */
public class HistoryRecommendationGenerator extends UntypedActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerRecommendationRequest) {
            PeerRecommendationRequest request = 
                    (PeerRecommendationRequest) message;
            this.processPeerRecommendationRequest(request);
        }
        else if (message instanceof ViewHistoryResponse) {
            ViewHistoryResponse viewHistoryResponse =
                    (ViewHistoryResponse) message;
            this.processViewHistoryResponse(viewHistoryResponse);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    /**
     * Responds to request by requesting view history
     * @param request
     */
    protected void processPeerRecommendationRequest(PeerRecommendationRequest request) {
        ViewHistoryRequest historyRequest = new ViewHistoryRequest(request);
        
        ActorSelection viewHistorian = getContext().actorSelection("user/viewHistorian");
        viewHistorian.tell(historyRequest, getSelf());
    }
    
    /**
     * Sends a recommendation based on this peer's view history back to this peer's recommender
     * Destined to be sent back to the original peer requesting this recommendation
     * @param response
     */
    protected void processViewHistoryResponse(ViewHistoryResponse response) {
        ViewHistory viewHistory = response.getViewHistory();
        PeerRecommendation peerRecommendation = 
                this.getPeerRecommendationBasedOnHistory(viewHistory);
        
        ActorSelection recommender = getContext().actorSelection("user/recommender");
        recommender.tell(peerRecommendation, getSelf());
    }
    
    /**
     * Creates Peer Recommendation based on View History
     * @param request
     * @return
     */
    private PeerRecommendation getPeerRecommendationBasedOnHistory(ViewHistory viewHistory) {
        
        
        return null;
    }
}
