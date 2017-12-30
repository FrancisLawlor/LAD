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
    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        ActorRef peerLinker = actorSystem.actorOf(Props.create(DummyPeerLinker.class), "PeerLinker");
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(new UniversalId("Peer3"), "PeerLinker");
        peerLinker.tell(peerIdInit, ActorRef.noSender());
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit loggerInit = new DummyInit(logger);
        peerLinker.tell(loggerInit, ActorRef.noSender());
        peerLinker.tell(new StartTest(), ActorRef.noSender());
        
        Thread.sleep(10000);
        
        peerLinker.tell(new EndTest(), ActorRef.noSender());
        
        Thread.sleep(4000);
        
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
