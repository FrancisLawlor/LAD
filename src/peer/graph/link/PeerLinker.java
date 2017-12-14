package peer.graph.link;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import akka.actor.ActorRef;
import akka.actor.Props;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.Weighter;

/**
 * Manages peer links
 * Records most important links
 * Keeps reserve of random low weight links for diversity
 * Sends Peer IDs back from recorded links
 *
 */
public class PeerLinker extends PeerToPeerActor {
    private Set<UniversalId> peerLinksIds;
    
    /**
     * Creates a Set to contain the Universal IDs of known peers
     */
    public PeerLinker() {
        this.peerLinksIds = new HashSet<UniversalId>();
    }
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof PeerLinkAddition) {
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
        UniversalId peerId = addition.getPeerId();
        
        this.peerLinksIds.add(peerId);
        
        String weighterName = "weighter_" + peerId.toString();
        ActorRef weighter = getContext().actorOf(Props.create(Weighter.class), weighterName);
        
        LocalWeightUpdateRequest request = new LocalWeightUpdateRequest(peerId, addition.getStartingWeight());
        weighter.tell(request, getSelf());
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
        Iterator<UniversalId> peerLinksIdsIt = this.peerLinksIds.iterator();
        while (peerLinksIdsIt.hasNext()) {
            UniversalId peerLinksId = peerLinksIdsIt.next();
            PeerLinkResponse response = new PeerLinkResponse(peerLinksId);
            sender.tell(response, sender);
        }
    }
}
