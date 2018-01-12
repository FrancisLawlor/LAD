package tests.content.recommend;

import akka.actor.ActorRef;
import akka.actor.Props;
import content.recommend.actors.PeerRecommendationAggregator;
import content.recommend.heuristic.DeterministicAggregationHeuristic;
import content.recommend.messages.PeerRecommendationAggregatorInit;
import content.recommend.messages.RecommendationsForUserRequest;
import peer.frame.core.ActorNames;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
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
