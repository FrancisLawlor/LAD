package tests.content.recommend;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.distributedmap.PeerWeightedLinkAddition;
import peer.graph.distributedmap.PeerWeightedLinkorDHM;
import peer.graph.weight.Weight;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestPeerRecommendationAggregatorDHM {
    private static final String PEER_ONE = "Peer1";
    private static final String PEER_TWO = "Peer2";
    private static final String PEER_THREE = "Peer3";
    private static final String PEER_FOUR = "Peer4";
    
    public static void main(String[] args) throws Exception {
        ActorSystem actSys = ActorSystem.create("ContentSystem");
        UniversalId peerOneId = new UniversalId(PEER_ONE);
        UniversalId peerTwoId = new UniversalId(PEER_TWO);
        UniversalId peerThreeId = new UniversalId(PEER_THREE);
        UniversalId peerFourId = new UniversalId(PEER_FOUR);
        
        final ActorRef peerWeightedLinkor = actSys.actorOf(Props.create(PeerWeightedLinkorDHM.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerOneId, ActorNames.PEER_LINKER);
        peerWeightedLinkor.tell(init, ActorRef.noSender());
        
        //Add links
        PeerWeightedLinkAddition addition = new PeerWeightedLinkAddition(peerTwoId, new Weight(2.0));
        peerWeightedLinkor.tell(addition, ActorRef.noSender());
        addition = new PeerWeightedLinkAddition(peerThreeId, new Weight(3.0));
        peerWeightedLinkor.tell(addition, ActorRef.noSender());
        addition = new PeerWeightedLinkAddition(peerFourId, new Weight(5.0));
        peerWeightedLinkor.tell(addition, ActorRef.noSender());
        Thread.sleep(1000);
        
        // Create logger and put in Init message
        final ActorTestLogger logger = new ActorTestLogger();
        DummyInit dummyInit = new DummyInit(logger);
        
        final ActorRef dummyOutboundCommunicator = actSys.actorOf(Props.create(DummyOutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        init = new PeerToPeerActorInit(peerOneId, ActorNames.OUTBOUND_COMM);
        dummyOutboundCommunicator.tell(init, ActorRef.noSender());
        dummyOutboundCommunicator.tell(dummyInit, ActorRef.noSender());
        
        final ActorRef dummyViewer = actSys.actorOf(Props.create(DummyViewer.class), ActorNames.VIEWER);
        init = new PeerToPeerActorInit(peerOneId, ActorNames.VIEWER);
        dummyViewer.tell(init, ActorRef.noSender());
        dummyViewer.tell(dummyInit, ActorRef.noSender());
        
        final ActorRef recommender = actSys.actorOf(Props.create(DummyRecommenderForAggregatorDHMTest.class), ActorNames.RECOMMENDER);
        init = new PeerToPeerActorInit(peerOneId, ActorNames.RECOMMENDER);
        recommender.tell(init, ActorRef.noSender());
        recommender.tell(dummyInit, ActorRef.noSender());
        
        recommender.tell(new StartTest(), ActorRef.noSender()); // Starts test
        Thread.sleep(1000);
        
        //Print out messages
        for (String message : logger) {
            System.out.println(message);
        }
    }
}