package tests.content.view;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.view.actors.ViewHistorian;
import peer.frame.core.ActorNames;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestViewHistorian {
    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        UniversalId peerId = new UniversalId("PeerOne");
        
        final ActorRef viewHistorian = actorSystem.actorOf(Props.create(ViewHistorian.class), ActorNames.VIEW_HISTORIAN);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerId, ActorNames.VIEW_HISTORIAN);
        viewHistorian.tell(init, ActorRef.noSender());
        
        final ActorRef dummyDatabaser = actorSystem.actorOf(Props.create(DummyDatabaser.class), ActorNames.DATABASER);
        init = new PeerToPeerActorInit(peerId, ActorNames.DATABASER);
        dummyDatabaser.tell(init, ActorRef.noSender());
        
        final ActorRef testor = actorSystem.actorOf(Props.create(ViewHistorianTestor.class), "Testor");
        init = new PeerToPeerActorInit(peerId, "Testor");
        testor.tell(init, ActorRef.noSender());
        
        // Logger
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit dummyInit = new DummyInit(logger);
        dummyDatabaser.tell(dummyInit, ActorRef.noSender());
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
