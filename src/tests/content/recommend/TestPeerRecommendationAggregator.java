package tests.content.recommend;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.Recommender;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

@SuppressWarnings("unused")
public class TestPeerRecommendationAggregator {
    public static void main(String[] args) throws Exception {
        ActorSystem actSys = ActorSystem.create("ContentSystem");
        UniversalId peerOneId = new UniversalId("Peer1");
        UniversalId peerTwoId = new UniversalId("Peer2");
        UniversalId peerThreeId = new UniversalId("Peer3");
        UniversalId peerFourId = new UniversalId("Peer4");
        
        // Create logger and put in Init message
        final ActorTestLogger logger = new ActorTestLogger();
        DummyInit dummyInit = new DummyInit(logger);
        
        final ActorRef dummyPeerLinker = actSys.actorOf(Props.create(DummyPeerLinker.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerOneId, ActorNames.PEER_LINKER);
        dummyPeerLinker.tell(init, null);
        dummyPeerLinker.tell(dummyInit, null);
        
        final ActorRef dummyOutboundCommunicator = actSys.actorOf(Props.create(DummyOutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        init = new PeerToPeerActorInit(peerOneId, ActorNames.OUTBOUND_COMM);
        dummyOutboundCommunicator.tell(init, null);
        dummyOutboundCommunicator.tell(dummyInit, null);
        
        final ActorRef dummyViewer = actSys.actorOf(Props.create(DummyViewer.class), ActorNames.VIEWER);
        init = new PeerToPeerActorInit(peerOneId, ActorNames.VIEWER);
        dummyViewer.tell(init, null);
        dummyViewer.tell(dummyInit, null);
        
        final ActorRef recommender = actSys.actorOf(Props.create(DummyRecommenderForAggregatorTest.class), ActorNames.RECOMMENDER);
        init = new PeerToPeerActorInit(peerOneId, ActorNames.RECOMMENDER);
        recommender.tell(init, null);
        recommender.tell(dummyInit, null);
        
        recommender.tell(new StartTest(), null); // Starts test
        
        // Waits 10 seconds for the test to complete
        Thread.sleep(10000);
        
        //Print out messages
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
