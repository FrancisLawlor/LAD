package tests.gui.core;

import akka.actor.ActorRef;
import akka.actor.Props;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
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
}
