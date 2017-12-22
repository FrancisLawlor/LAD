package peer.graph.link;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import akka.actor.ActorRef;
import akka.actor.Props;
import core.ActorNames;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import core.xcept.UnknownMessageException;
import peer.graph.weight.Weighter;
import peer.graph.weight.WeighterInit;

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
        else if (message instanceof PeerLinkExistenceRequest) {
            PeerLinkExistenceRequest request = (PeerLinkExistenceRequest) message;
            this.processPeerLinkExistenceRequest(request);
        }
        else if (message instanceof PeerLinksRequest) {
            PeerLinksRequest peerLinksRequest =
                    (PeerLinksRequest) message;
            this.processPeerLinksRequest(peerLinksRequest);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Adds new theoretical link between this peer and another
     * @param addition
     */
    protected void processPeerLinkAddition(PeerLinkAddition addition) {
        UniversalId peerId = addition.getPeerId();
        
        if (!this.peerLinksIds.contains(peerId)) {
            this.peerLinksIds.add(peerId);
            
            ActorRef weighter = getContext().actorOf(Props.create(Weighter.class), ActorNames.getWeighterName(peerId));
            WeighterInit init = new WeighterInit(peerId, addition.getStartingWeight());
            weighter.tell(init, getSelf());
        }
    }
    
    /**
     * Checks for the existence of a stored link between this peer and another specified peer
     * @param request
     */
    protected void processPeerLinkExistenceRequest(PeerLinkExistenceRequest request) {
        UniversalId linkToCheck = request.getLinkToCheckPeerId();
        boolean exists = this.peerLinksIds.contains(linkToCheck);
        PeerLinkExistenceResponse response = new PeerLinkExistenceResponse(linkToCheck, exists);
        ActorRef sender = getSender();
        sender.tell(response, sender);
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
            sender.tell(response, getSelf());
        }
    }
}
