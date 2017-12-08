package content.recommend;


import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import content.recommend.heuristic.HistoryHeuristic;
import content.view.ViewHistoryRequest;
import content.view.ViewHistoryResponse;
import core.xcept.UnknownMessageException;

/**
 * Generates Recommendation from this peer based on View History
 *
 */
public class HistoryRecommendationGenerator extends UntypedActor {
    private HistoryHeuristic heuristic;
    
    public HistoryRecommendationGenerator(HistoryHeuristic heuristic) {
        this.heuristic = heuristic;
    }
    
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
            throw new UnknownMessageException();
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
        PeerRecommendation peerRecommendation = 
                this.getPeerRecommendationBasedOnHistory(response);
        
        ActorSelection recommender = getContext().actorSelection("user/recommender");
        recommender.tell(peerRecommendation, getSelf());
    }
    
    /**
     * Creates Peer Recommendation based on View History and HistoryHeuristic
     * @param request
     * @return
     */
    private PeerRecommendation getPeerRecommendationBasedOnHistory(ViewHistoryResponse viewHistoryResponse) {
        PeerRecommendation recommendation = this.heuristic.getRecommendation(viewHistoryResponse);
        return recommendation;
    }
}
