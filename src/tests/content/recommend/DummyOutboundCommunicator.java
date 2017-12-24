package tests.content.recommend;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorSelection;
import content.core.Content;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.recommend.Recommendation;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyOutboundCommunicator extends DummyActor {
    private static final String PEER_TWO = "Peer2";
    private static final String PEER_THREE = "Peer3";
    private static final String PEER_FOUR = "Peer4";
    private List<Content> contentListPeerTwo;
    private List<Content> contentListPeerThree;
    private List<Content> contentListPeerFour;
    
    public DummyOutboundCommunicator() {
        this.contentListPeerTwo = new LinkedList<Content>();
        this.contentListPeerTwo.add(new Content("2a", "2a", "2a", null, 2));
        this.contentListPeerTwo.add(new Content("2b", "2b", "2b", null, 2));
        this.contentListPeerThree = new LinkedList<Content>();
        this.contentListPeerThree.add(new Content("3a", "3a", "3a", null, 3));
        this.contentListPeerThree.add(new Content("3b", "3b", "3b", null, 3));
        this.contentListPeerFour = new LinkedList<Content>();
        this.contentListPeerFour.add(new Content("4a", "4a", "4a", null, 4));
        this.contentListPeerFour.add(new Content("4b", "4b", "4b", null, 4));
    }
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof PeerRecommendationRequest) {
            PeerRecommendationRequest request = (PeerRecommendationRequest) message;
            this.processPeerRecommendationRequest(request);
        }
        else if (message instanceof PeerRecommendation) {
            PeerRecommendation recommendation = (PeerRecommendation) message;
            this.processPeerRecommendation(recommendation);
        }
    }
    
    protected void processPeerRecommendationRequest(PeerRecommendationRequest request) {
        super.logger.logMessage("Received PeerRecommendationRequest in OutBoundCommunicator");
        
        ActorSelection aggregator = getContext().actorSelection(ActorPaths.getPathToAggregator());
        PeerRecommendation recommendation;
        
        if (request.getOriginalTarget().toString().equals(PEER_TWO)) {
            super.logger.logMessage("Sending back fake recommendation from " + PEER_TWO);
            for (Content content : contentListPeerTwo) {
                super.logger.logMessage("Content Id: " + content.getId());
                super.logger.logMessage("Content Name: " + content.getFileName());
                super.logger.logMessage("Content Type: " + content.getFileFormat());
                super.logger.logMessage("Content Length: " + content.getViewLength());
                super.logger.logMessage("");
            }
            recommendation = new PeerRecommendation(contentListPeerTwo, request.getOriginalRequester(), request.getOriginalTarget());
            aggregator.tell(recommendation, getSelf());
        }
        else if (request.getOriginalTarget().toString().equals(PEER_THREE)) {
            super.logger.logMessage("Sending back fake recommendation from " + PEER_THREE);
            for (Content content : contentListPeerThree) {
                super.logger.logMessage("Content Id: " + content.getId());
                super.logger.logMessage("Content Name: " + content.getFileName());
                super.logger.logMessage("Content Type: " + content.getFileFormat());
                super.logger.logMessage("Content Length: " + content.getViewLength());
                super.logger.logMessage("");
            }
            recommendation = new PeerRecommendation(contentListPeerThree, request.getOriginalRequester(), request.getOriginalTarget());
            aggregator.tell(recommendation, getSelf());
        }
        else if (request.getOriginalTarget().toString().equals(PEER_FOUR)) {
            super.logger.logMessage("Sending back fake recommendation from " + PEER_FOUR);
            for (Content content : contentListPeerFour) {
                super.logger.logMessage("Content Id: " + content.getId());
                super.logger.logMessage("Content Name: " + content.getFileName());
                super.logger.logMessage("Content Type: " + content.getFileFormat());
                super.logger.logMessage("Content Length: " + content.getViewLength());
                super.logger.logMessage("");
            }
            recommendation = new PeerRecommendation(contentListPeerFour, request.getOriginalRequester(), request.getOriginalTarget());
            aggregator.tell(recommendation, getSelf());
        }
    }
    
    protected void processPeerRecommendation(PeerRecommendation peerRecommendation) {
        super.logger.logMessage("Received PeerRecommendation in OutBoundCommunicator");
        super.logger.logMessage("Message type: " + peerRecommendation.getType().toString());
        int i = 1;
        for (Recommendation recommendation : peerRecommendation) {
            super.logger.logMessage("Recommendation no. " + i++ + " received in OutboundCommunicator");
            super.logger.logMessage("Content ID: " + recommendation.getContentId());
            super.logger.logMessage("Content Name: " + recommendation.getContentName());
            super.logger.logMessage("Content Type: " + recommendation.getContentType());
            super.logger.logMessage("Content Length: " + recommendation.getContentLength());
            super.logger.logMessage("");
        }
        super.logger.logMessage("\n");
    }
}
