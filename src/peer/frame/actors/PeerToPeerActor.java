package peer.frame.actors;

import akka.actor.UntypedActor;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;

/**
 * Superclass for Actors implementing this Peer to Peer Distributed Recommender System
 * Contains information inherited by all Peer To Peer actors implementing this system
 *
 */
public abstract class PeerToPeerActor extends UntypedActor {
    protected UniversalId peerId;
    protected String nameOfActorInSystem;
    protected boolean alreadyInitialised = false;
    
    /**
     * Initialises the PeerToPeerActor superclass with information inherited by all others
     * @param init
     */
    protected void initialisePeerToPeerActor(PeerToPeerActorInit init) {
        if (!this.alreadyInitialised) {
            this.alreadyInitialised = true;
            this.peerId = init.getPeerId();
            this.nameOfActorInSystem = init.getActorName();
        }
    }
}
