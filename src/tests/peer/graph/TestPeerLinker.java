package tests.peer.graph;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.link.PeerLinker;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestPeerLinker {
    public static final String TESTOR_NAME = "PeerLinkerTestor";
    
    public static void main(String[] args) throws Exception {
        UniversalId peerOneId = new UniversalId("PeerOne");
        
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        
        final ActorRef peerLinker = actorSystem.actorOf(Props.create(PeerLinker.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(peerOneId, ActorNames.PEER_LINKER);
        peerLinker.tell(peerIdInit, null);
        
        final ActorRef peerLinkTestor = actorSystem.actorOf(Props.create(PeerLinkerTestor.class), TESTOR_NAME);
        peerIdInit = new PeerToPeerActorInit(peerOneId, TESTOR_NAME);
        peerLinkTestor.tell(peerIdInit, null);
        
        // Logger
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit loggerInit = new DummyInit(logger);
        peerLinkTestor.tell(loggerInit, null);
        
        // Start Test
        peerLinkTestor.tell(new StartTest(), null);
        
        // Wait 10 seconds for Test to complete
        Thread.sleep(10000);
        
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
