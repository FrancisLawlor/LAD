package tests.peer.graph;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.distributedmap.PeerWeightedLinkorDHM;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestPeerWeightedLinkorDHM {
    public static final String TESTOR_NAME = "PeerWeightedLinkorTestor";
    
    public static void main(String[] args) throws Exception {
        UniversalId peerOneId = new UniversalId(PeerWeightedLinkorDHMTestor.PEER_ONE);
        
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        
        final ActorRef peerWeightedLinkor = actorSystem.actorOf(Props.create(PeerWeightedLinkorDHM.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.PEER_LINKER);
        peerWeightedLinkor.tell(peerIdInit, null);
        
        final ActorRef peerWeightedLinkorTestor = actorSystem.actorOf(Props.create(PeerWeightedLinkorDHMTestor.class), TESTOR_NAME);
        peerIdInit = new PeerToPeerActorInit(peerOneId, TESTOR_NAME);
        peerWeightedLinkorTestor.tell(peerIdInit, null);
        
        final ActorRef dummyOutboundComm = actorSystem.actorOf(Props.create(DummyOutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.OUTBOUND_COMM);
        dummyOutboundComm.tell(peerIdInit, null);
        
        // Logger
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit loggerInit = new DummyInit(logger);
        peerWeightedLinkorTestor.tell(loggerInit, null);
        dummyOutboundComm.tell(loggerInit, null);
        
        // Start Test
        peerWeightedLinkorTestor.tell(new StartTest(), null);
        Thread.sleep(1000);
        peerWeightedLinkorTestor.tell(new StartTest(), null);
        Thread.sleep(1000);
        peerWeightedLinkorTestor.tell(new StartTest(), null);
        Thread.sleep(1000);
        peerWeightedLinkorTestor.tell(new StartTest(), null);
        Thread.sleep(1000);
        peerWeightedLinkorTestor.tell(new StartTest(), null);
        Thread.sleep(1000);
        peerWeightedLinkorTestor.tell(new StartTest(), null);
        Thread.sleep(1000);
        peerWeightedLinkorTestor.tell(new StartTest(), null);
        Thread.sleep(1000);
        
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
