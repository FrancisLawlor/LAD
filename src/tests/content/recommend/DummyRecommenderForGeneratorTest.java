package tests.content.recommend;

import akka.actor.ActorRef;
import akka.actor.Props;
import content.recommend.HistoryRecommendationGenerator;
import content.recommend.HistoryRecommendationGeneratorInit;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.recommend.Recommendation;
import content.recommend.heuristic.DeterministicHistoryHeuristic;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class DummyRecommenderForGeneratorTest extends DummyActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            DummyInit init = (DummyInit) message;
            super.logger = init.getLogger();
        }
        else if (message instanceof PeerRecommendation) {
            PeerRecommendation recommendation = (PeerRecommendation) message;
            this.processPeerRecommendation(recommendation);
        }
        else if (message instanceof StartTest) {
            this.startGeneratorTest();
        }
    }
    
    protected void startGeneratorTest() {
        UniversalId peerOneId = new UniversalId("Peer1");
        UniversalId peerTwoId = new UniversalId("Peer2");
        final ActorRef generatorToTest = getContext().actorOf(Props.create(HistoryRecommendationGenerator.class), ActorNames.getHistoryGeneratorName(peerOneId));
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerTwoId, ActorNames.getHistoryGeneratorName(peerOneId));
        HistoryRecommendationGeneratorInit generatorInit = new HistoryRecommendationGeneratorInit(peerOneId, new DeterministicHistoryHeuristic());
        generatorToTest.tell(init, null);
        generatorToTest.tell(generatorInit, null);
        
        //Start test
        generatorToTest.tell(new PeerRecommendationRequest(peerOneId, peerTwoId), null);
    }
    
    protected void processPeerRecommendation(PeerRecommendation peerRecommendation) {
        super.logger.logMessage("Received PeerRecommendation in Recommender back from HistoryRecommendationGenerator");
        super.logger.logMessage("Message type: " + peerRecommendation.getType().toString());
        int i = 1;
        for (Recommendation recommendation : peerRecommendation) {
            super.logger.logMessage("Recommendation no. " + i++ + ":");
            super.logger.logMessage("Content ID: " + recommendation.getContentId());
            super.logger.logMessage("Content Name: " + recommendation.getContentName());
            super.logger.logMessage("Content Type: " + recommendation.getContentType());
            super.logger.logMessage("Content Length: " + recommendation.getContentLength());
            super.logger.logMessage("\n");
        }
        super.logger.logMessage("\n");
    }
}
