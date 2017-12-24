package tests.gui.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.camel.CamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.RecommendationsForUser;
import content.retrieve.RetrievedContent;
import content.view.Viewer;
import content.view.ViewerInit;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.PeerToPeerActorSystem;
import peer.core.UniversalId;
import peer.core.ViewerToUIChannel;

@SuppressWarnings("unused")
public class TestPeerToPeerActorSystem1 extends PeerToPeerActorSystem {    
    public TestPeerToPeerActorSystem1(UniversalId peerId) {
        super(peerId);
    }
    
    @Override
    protected void initialiseRecommendingSystem() throws Exception {
        final ActorRef recommender = this.actorSystem.actorOf(Props.create(DummyRecommender.class), ActorNames.RECOMMENDER);
    }
}
