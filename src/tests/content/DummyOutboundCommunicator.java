package tests.content;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorSelection;
import content.impl.Content;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import core.ActorPaths;
import core.PeerToPeerActorInit;

public class DummyOutboundCommunicator extends DummyActor {
    private List<Content> contentListPeerTwo;
    private List<Content> contentListPeerThree;
    private List<Content> contentListPeerFour;
    
    public DummyOutboundCommunicator() {
        this.contentListPeerTwo = new LinkedList<Content>();
        this.contentListPeerTwo.add(new Content("2a", "2a", "2a", null, 0));
        this.contentListPeerTwo.add(new Content("2b", "2b", "2b", null, 0));
        this.contentListPeerThree = new LinkedList<Content>();
        this.contentListPeerThree.add(new Content("3a", "3a", "3a", null, 0));
        this.contentListPeerThree.add(new Content("3b", "3b", "3b", null, 0));
        this.contentListPeerFour = new LinkedList<Content>();
        this.contentListPeerFour.add(new Content("4a", "4a", "4a", null, 0));
        this.contentListPeerFour.add(new Content("4b", "4b", "4b", null, 0));
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
        
        if (request.getOriginalTarget().toString().equals("Peer2")) {
            super.logger.logMessage("Sending back fake recommendation from Peer2");
            recommendation = new PeerRecommendation(contentListPeerTwo, request.getOriginalRequester(), request.getOriginalTarget());
            aggregator.tell(recommendation, getSelf());
        }
        else if (request.getOriginalTarget().toString().equals("Peer3")) {
            super.logger.logMessage("Sending back fake recommendation from Peer3");
            recommendation = new PeerRecommendation(contentListPeerThree, request.getOriginalRequester(), request.getOriginalTarget());
            aggregator.tell(recommendation, getSelf());
        }
        else if (request.getOriginalTarget().toString().equals("Peer4")) {
            super.logger.logMessage("Sending back fake recommendation from Peer4");
            recommendation = new PeerRecommendation(contentListPeerFour, request.getOriginalRequester(), request.getOriginalTarget());
            aggregator.tell(recommendation, getSelf());
        }
    }
    
    protected void processPeerRecommendation(PeerRecommendation recommendation) {
        
    }
}