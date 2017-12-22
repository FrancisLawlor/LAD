package tests.content.recommend;

import akka.actor.ActorRef;
import akka.actor.Props;
import content.impl.Content;
import content.recommend.HistoryRecommendationGenerator;
import content.recommend.HistoryRecommendationGeneratorInit;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.recommend.heuristic.WeightedProbabilityHistoryHeuristic;
import core.ActorNames;
import core.PeerToPeerActorInit;
import core.UniversalId;
import tests.actors.DummyActor;
import tests.actors.DummyInit;

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
        final ActorRef generatorToTest = getContext().actorOf(Props.create(HistoryRecommendationGenerator.class), ActorNames.HISTORY_GENERATOR);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerTwoId, ActorNames.HISTORY_GENERATOR);
        HistoryRecommendationGeneratorInit generatorInit = new HistoryRecommendationGeneratorInit(peerOneId, new WeightedProbabilityHistoryHeuristic());
        generatorToTest.tell(init, null);
        generatorToTest.tell(generatorInit, null);
        
        //Start test
        generatorToTest.tell(new PeerRecommendationRequest(peerOneId, peerTwoId), null);
    }
    
    protected void processPeerRecommendation(PeerRecommendation recommendation) {
        super.logger.logMessage("Received PeerRecommendation in OutBoundCommunicator");
        int i = 1;
        for (Content content : recommendation) {
            super.logger.logMessage("Recommendation no. " + i++ + " :" + content.getFileName());
        }
    }
}
