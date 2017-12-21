package content.recommend;


import java.util.List;

import akka.actor.ActorSelection;
import content.impl.Content;
import content.recommend.heuristic.HistoryHeuristic;
import content.view.ViewHistoryRequest;
import content.view.ViewHistoryResponse;
import core.ActorNames;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import core.xcept.PeerRecommendationRequestIdMismatchException;
import core.xcept.UnknownMessageException;
import core.xcept.WrongPeerIdException;

/**
 * Generates Recommendation from this peer based on View History
 *
 */
public class HistoryRecommendationGenerator extends PeerToPeerActor {
    private UniversalId requestingPeer;
    private HistoryHeuristic heuristic;
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof HistoryRecommendationGeneratorInit) {
            HistoryRecommendationGeneratorInit init = (HistoryRecommendationGeneratorInit) message;
            this.requestingPeer = init.getRequestingPeerId();
            this.heuristic = init.getHeuristic();
        }
        else if (message instanceof PeerRecommendationRequest) {
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
        if (!request.getOriginalTarget().equals(super.peerId)) throw new WrongPeerIdException();
        if (!this.requestingPeer.equals(request.getOriginalRequester()))
            throw new PeerRecommendationRequestIdMismatchException();
        
        ViewHistoryRequest historyRequest = new ViewHistoryRequest(request);
        
        ActorSelection viewHistorian = getContext().actorSelection("user/" + ActorNames.VIEW_HISTORIAN);
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
        
        ActorSelection recommender = getContext().actorSelection("user/" + ActorNames.RECOMMENDER);
        recommender.tell(peerRecommendation, getSelf());
    }
    
    /**
     * Creates Peer Recommendation based on View History and HistoryHeuristic
     * @param request
     * @return
     */
    private PeerRecommendation getPeerRecommendationBasedOnHistory(ViewHistoryResponse viewHistoryResponse) {
        PeerRecommendation recommendation;
        List<Content> contentList = this.heuristic.getRecommendation(viewHistoryResponse);
        recommendation = new PeerRecommendation(contentList, this.requestingPeer, super.peerId);
        return recommendation;
    }
}
