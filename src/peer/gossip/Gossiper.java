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
        else if (message instanceof AddressChangeAnnounce) {
            AddressChangeAnnounce announcement = (AddressChangeAnnounce) message;
            this.processAddressChangeAnnounce(announcement);
        }
        else if (message instanceof AddressChangedAcknowledged) {
            AddressChangedAcknowledged acknowledgement = (AddressChangedAcknowledged) message;
            this.processAddressChangedAcknowledged(acknowledgement);
        }
        else if (message instanceof OldPeerAddressResponse) {
            OldPeerAddressResponse oldPeerAddress = (OldPeerAddressResponse) message;
            this.processOldPeerAddressResponse(oldPeerAddress);
        }
        else if (message instanceof ResolvePeerAddressRequest) {
            ResolvePeerAddressRequest resolveRequest = (ResolvePeerAddressRequest) message;
            this.processResolvePeerAddressRequest(resolveRequest);
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
    
    /**
     * 
     * @param announcement
     */
    protected void processAddressChangeAnnounce(AddressChangeAnnounce announcement) {
        
    }
    
    /**
     * 
     * @param acknowledgement
     */
    protected void processAddressChangedAcknowledged(AddressChangedAcknowledged acknowledgement) {
        
    }
    
    /**
     * 
     * @param oldPeerAddress
     */
    protected void processOldPeerAddressResponse(OldPeerAddressResponse oldPeerAddress) {
        
    }
    
    /**
     * 
     * @param resolveRequest
     */
    protected void processResolvePeerAddressRequest(ResolvePeerAddressRequest resolveRequest) {
        
    }
}
