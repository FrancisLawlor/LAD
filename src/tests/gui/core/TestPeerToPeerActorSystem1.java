package tests.gui.core;

import org.apache.camel.CamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.view.Viewer;
import core.ActorNames;
import core.PeerToPeerActorInit;
import core.UniversalId;

@SuppressWarnings("unused")
public class TestPeerToPeerActorSystem1 {
    protected UniversalId peerId;
    protected ActorSystem actorSystem;
    protected CamelContext camelContext;
    protected ActorRef viewer;
    
    public TestPeerToPeerActorSystem1(UniversalId peerId) {        
        actorSystem = ActorSystem.create("ContentSystem");
    }
    
    public void createActors() throws Exception {
        initialiseViewingSystem();
        initialiseRecommendingSystem();
    }
    
    public ActorRef getViewer() {
        return this.viewer;
    }
    
    protected void initialiseViewingSystem() throws Exception {
        viewer = actorSystem.actorOf(Props.create(Viewer.class), ActorNames.VIEWER);
        PeerToPeerActorInit viewerActorInit = new PeerToPeerActorInit(peerId, ActorNames.VIEWER);
        viewer.tell(viewerActorInit, null);
    }
    
    protected void initialiseRecommendingSystem() throws Exception {
        final ActorRef recommender = actorSystem.actorOf(Props.create(DummyRecommender.class), ActorNames.RECOMMENDER);
    }
}
