package tests.adt;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestDistributedMap {
    public static final int TEST_SIZE = 3000;
    
    public static void main(String[] args) throws Exception {
        System.out.println("Testing " + TEST_SIZE + " entries in the Distributed Hash Map.");
        System.out.println("Requests have been spaced to avoid OverLoading.");
        System.out.println("Please allow 2 minutes for the test");
        
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        ActorRef peerLinker = actorSystem.actorOf(Props.create(DummyPeerLinker.class), "PeerLinker");
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(new UniversalId("Peer3"), "PeerLinker");
        peerLinker.tell(peerIdInit, ActorRef.noSender());
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit loggerInit = new DummyInit(logger);
        peerLinker.tell(loggerInit, ActorRef.noSender());
        Thread.sleep(10000);
        peerLinker.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(10000);
        peerLinker.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(10000);
        peerLinker.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(10000);
        peerLinker.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(10000);
        peerLinker.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(10000);
        peerLinker.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(10000);
        peerLinker.tell(new EndTest(), ActorRef.noSender());
        Thread.sleep(5000);
        
        for (String message : logger) {
            System.out.println(message);
            Thread.sleep(1);
        }
    }
}
