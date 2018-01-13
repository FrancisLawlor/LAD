package tests.content.similarity;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.similarity.actors.Similaritor;
import peer.frame.core.ActorNames;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;
import tests.core.StartTest;

public class TestSimilaritor {
    public static final String PEER_ONE = "PeerOne";
    
    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        UniversalId peerId = new UniversalId(PEER_ONE);
        
        final ActorRef similaritor = actorSystem.actorOf(Props.create(Similaritor.class), ActorNames.SIMILARITOR);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerId, ActorNames.SIMILARITOR);
        similaritor.tell(init, ActorRef.noSender());
        
        final ActorRef dummyDatabaser = actorSystem.actorOf(Props.create(DummyDatabaser.class), ActorNames.DATABASER);
        init = new PeerToPeerActorInit(peerId, ActorNames.DATABASER);
        dummyDatabaser.tell(init, ActorRef.noSender());
        
        final ActorRef dummyPeerWeightedLinkor = actorSystem.actorOf(Props.create(DummyPeerWeightedLinkor.class), ActorNames.PEER_LINKER);
        init = new PeerToPeerActorInit(peerId, ActorNames.PEER_LINKER);
        dummyPeerWeightedLinkor.tell(init, ActorRef.noSender());
        
        final ActorRef dummyRetriever = actorSystem.actorOf(Props.create(DummyRetriever.class), ActorNames.RETRIEVER);
        init = new PeerToPeerActorInit(peerId, ActorNames.RETRIEVER);
        dummyRetriever.tell(init, ActorRef.noSender());
        
        // Logger for Dummy Actors
        ActorTestLogger logger = new ActorTestLogger();
        DummyInit dummyInit = new DummyInit(logger);
        dummyDatabaser.tell(dummyInit, ActorRef.noSender());
        dummyPeerWeightedLinkor.tell(dummyInit, ActorRef.noSender());
        dummyRetriever.tell(dummyInit, ActorRef.noSender());
        
        // Start test
        dummyRetriever.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(2000);
        dummyRetriever.tell(new StartTest(), ActorRef.noSender());
        Thread.sleep(2000);
        dummyRetriever.tell(new StartTest(), ActorRef.noSender());
        
        // Wait for test to complete
        Thread.sleep(10000);
        
        //Print Log
        for (String message : logger) {
            System.out.println(message);
        }
    }
}
