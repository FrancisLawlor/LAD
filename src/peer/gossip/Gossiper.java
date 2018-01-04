package peer.gossip;

import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.xcept.UnknownMessageException;

/**
 * Gossiper Actor
 *
 */
public class Gossiper extends PeerToPeerActor {
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof GossipInit) {
            GossipInit gossipInit = (GossipInit) message;
            this.processGossipInit(gossipInit);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * 
     * @param gossipInit
     */
    protected void processGossipInit(GossipInit gossipInit) {
        
    }
}
