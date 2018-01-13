package tests.gui.core;

import akka.actor.ActorRef;
import akka.actor.Props;
import peer.frame.core.ActorNames;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.TestPeerToPeerActorSystem;

public class TestPeerToPeerActorSystem1 extends TestPeerToPeerActorSystem {
    public TestPeerToPeerActorSystem1(UniversalId peerId) {
        super(peerId);
    }
    
    @Override
    protected void initialiseRecommendingSystem() throws Exception {
        final ActorRef recommender = this.actorSystem.actorOf(Props.create(DummyRecommender.class), ActorNames.RECOMMENDER);
        PeerToPeerActorInit init = new PeerToPeerActorInit(super.peerId, ActorNames.RECOMMENDER);
        recommender.tell(init, ActorRef.noSender());
    }
    
    @Override
    protected void initialiseRetrievingSystem() throws Exception {
        final ActorRef retriever = this.actorSystem.actorOf(Props.create(DummyRetriever.class), ActorNames.RETRIEVER);
        PeerToPeerActorInit init = new PeerToPeerActorInit(super.peerId, ActorNames.RETRIEVER);
        retriever.tell(init, ActorRef.noSender());
    }
    
    @Override
    protected void initialiseDatabase() {
        final ActorRef databaser = this.actorSystem.actorOf(Props.create(DummyDatabaser.class), ActorNames.DATABASER);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerId, ActorNames.INBOUND_COMM);
        databaser.tell(init, ActorRef.noSender());
    }
}
