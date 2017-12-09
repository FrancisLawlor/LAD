package peer.initialisation;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.Recommender;
import content.view.ViewHistorian;
import content.view.Viewer;
import peer.graph.link.PeerLinker;

/**
 * Initialises the permanent Actors for this Peer
 *
 */
public class Initialiser {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("ContentSystem");
        
        try {
            final ActorRef viewer = 
                    system.actorOf(Props.create(Viewer.class), "viewer");
            
            final ActorRef viewHistorian = 
                    system.actorOf(Props.create(ViewHistorian.class), "viewHistorian");
            
            final ActorRef peerLinker = 
                    system.actorOf(Props.create(PeerLinker.class), "peerLinker");
            
            // Loop over a file and tell peerLinker it's peers...
            
            // Loop over a file and create a weighter for each peer
            // While looping over file tell each weighter its weight
            
            final ActorRef recommender = 
                    system.actorOf(Props.create(Recommender.class), "recommender");
            
        } catch (Exception e) { };
    }
}
