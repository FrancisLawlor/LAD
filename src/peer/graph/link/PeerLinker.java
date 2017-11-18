package peer.graph.link;

import java.util.Iterator;
import java.util.Set;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * Manages peer links
 * Records most important links
 * Keeps reserve of random low weight links for diversity
 * Sends Peer IDs back from recorded links
 *
 */
public class PeerLinker extends UntypedActor {
    private Set<String> peerLinksIds;
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerLinkAddition) {
            PeerLinkAddition addition = 
                    (PeerLinkAddition) message;
            this.processPeerLinkAddition(addition);
        }
        else if (message instanceof PeerLinksRequest) {
            PeerLinksRequest peerLinksRequest =
                    (PeerLinksRequest) message;
            this.processPeerLinksRequest(peerLinksRequest);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    /**
     * Adds new theoretical link between this peer and another
     * @param addition
     */
    protected void processPeerLinkAddition(PeerLinkAddition addition) {
        
    }
    
    /**
     * Responds to request with Peer IDs that are linked to this peer
     * @param linksRequest
     */
    protected void processPeerLinksRequest(PeerLinksRequest linksRequest) {
        this.sendPeerLinks();
    }
    
    /**
     * Send back Peer IDs that are theoretically linked
     * 
     */
    private void sendPeerLinks() {
        ActorRef sender = getSender();
        Iterator<String> peerLinksIdsIt = this.peerLinksIds.iterator();
        while (peerLinksIdsIt.hasNext()) {
            String peerLinksId = peerLinksIdsIt.next();
            PeerLinkResponse response = new PeerLinkResponse(peerLinksId);
            sender.tell(response, sender);
        }
    }
}
