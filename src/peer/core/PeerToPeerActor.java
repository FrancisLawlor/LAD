package peer.core;

import akka.actor.UntypedActor;

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
