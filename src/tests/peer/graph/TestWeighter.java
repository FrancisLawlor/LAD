package tests.peer.graph;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestWeighter {
    public static void main(String[] args) throws Exception {
        UniversalId peerOneId = new UniversalId("PeerOne");
        
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        
        final ActorRef weighterTestor = actorSystem.actorOf(Props.create(WeighterTestor.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.PEER_LINKER);
        weighterTestor.tell(peerIdInit, null);
        
        final ActorRef dummyOutboundComm = actorSystem.actorOf(
                Props.create(DummyOutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.OUTBOUND_COMM);
        dummyOutboundComm.tell(peerIdInit, null);
        
        // Logger
        final ActorTestLogger logger = new ActorTestLogger();
        DummyInit loggerInit = new DummyInit(logger);
        weighterTestor.tell(loggerInit, null);
        dummyOutboundComm.tell(loggerInit, null);
        
        //Start Test
        weighterTestor.tell(new StartTest(), null);
        
        // Wait 10 seconds for Test to complete
        Thread.sleep(10000);
        
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
