package tests.peer.data;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.data.actors.Databaser;
import peer.frame.core.ActorNames;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestDatabaser {
    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        UniversalId peerId = new UniversalId("TestPeer");
        
        final ActorRef databaser = actorSystem.actorOf(Props.create(Databaser.class), ActorNames.DATABASER);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerId, ActorNames.DATABASER);
        databaser.tell(init, ActorRef.noSender());
        
        final ActorRef testor = actorSystem.actorOf(Props.create(DatabaserTestor.class), "Testor");
        init = new PeerToPeerActorInit(peerId, "Testor");
        testor.tell(init, ActorRef.noSender());
        
        // Logger
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit dummyInit = new DummyInit(logger);
        testor.tell(dummyInit, ActorRef.noSender());
        
        // Start Test
        testor.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(1000);
        testor.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(1000);
        testor.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(1000);
        testor.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(1000);
        testor.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(1000);
        
        Thread.sleep(5000);
        
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
