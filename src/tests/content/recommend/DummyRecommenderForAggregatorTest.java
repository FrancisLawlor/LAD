package tests.content.recommend;

import akka.actor.ActorRef;
import akka.actor.Props;
import content.recommend.PeerRecommendationAggregator;
import content.recommend.PeerRecommendationAggregatorInit;
import content.recommend.RecommendationsForUserRequest;
import content.recommend.heuristic.DeterministicAggregationHeuristic;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class DummyRecommenderForAggregatorTest extends DummyActor {
    
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
        else if (message instanceof StartTest) {
            this.startAggregatorTest();
        }
    }
    
    private void startAggregatorTest() {
        UniversalId peerOneId = new UniversalId("Peer1");
        
        ActorRef aggregatorToTest = getContext().actorOf(Props.create(PeerRecommendationAggregator.class), ActorNames.AGGREGATOR);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.AGGREGATOR);
        PeerRecommendationAggregatorInit init = new PeerRecommendationAggregatorInit(new DeterministicAggregationHeuristic());
        aggregatorToTest.tell(peerIdInit, null);
        aggregatorToTest.tell(init, null);
        
        aggregatorToTest.tell(new RecommendationsForUserRequest(peerOneId), null);
    }
}
