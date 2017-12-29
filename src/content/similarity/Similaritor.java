package content.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import akka.actor.ActorSelection;
import content.core.Content;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.link.PeerLinkAddition;
import peer.graph.link.PeerLinkExistenceRequest;
import peer.graph.link.PeerLinkExistenceResponse;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.Weight;

/**
 * Stores peers who viewed the same content to help retriever find peers who might have the content if it has already been deleted locally
 * Updates theoretical graph based on Header data from Retrieved Content
 * Updates PeerLinker and Weighters to increase weight of links between peers that viewed similar content to this peer
 *
 */
public class Similaritor extends PeerToPeerActor {
    private Map<Content, Set<UniversalId>> similarViewsPeers;
    private Map<UniversalId, Weight> weights;
    
    public Similaritor() {
        this.similarViewsPeers = new HashMap<Content, Set<UniversalId>>();
        this.weights = new HashMap<UniversalId, Weight>();
    }
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof SimilarContentViewPeerAlert) {
            SimilarContentViewPeerAlert alert = (SimilarContentViewPeerAlert) message;
            this.processSimilarContentViewPeerAlert(alert);
        }
        else if (message instanceof PeerLinkExistenceResponse) {
            PeerLinkExistenceResponse response = (PeerLinkExistenceResponse) message;
            this.processPeerLinkExistenceResponse(response);
        }
        else if (message instanceof SimilarContentViewPeerRequest) {
            SimilarContentViewPeerRequest request = (SimilarContentViewPeerRequest) message;
            this.processSimilarContentViewPeerRequest(request);
        }
    }
    
    /**
     * Stores the peers who viewed the same content as this peer
     * Helps retriever find other peers who might still have the content
     * If it has been deleted locally other peers who viewed the same content might still have it
     * Also reweighs the graph linking this peer to others based on this new information on the similarity of their views
     * @param alert
     */
    protected void processSimilarContentViewPeerAlert(SimilarContentViewPeerAlert alert) {
        UniversalId similarPeerId = alert.getSimilarViewPeerId();
        Content content = alert.getSimilarViewContent();
        if (this.similarViewsPeers.containsKey(content)) {
            Set<UniversalId> peerIdSet = this.similarViewsPeers.get(content);
            peerIdSet.add(similarPeerId);
        }
        else {
            Set<UniversalId> peerIdSet = new HashSet<UniversalId>();
            peerIdSet.add(similarPeerId);
            this.similarViewsPeers.put(content, peerIdSet);
        }
        
        this.reweightPeerLinkGraph(alert);
    }
    
    /**
     * Temporarily stores this peer's similarity weight for this similar view
     * Asks if peerLinker for the theoretical peer weight graph already has a link or not
     * How to add this weight to the graph depends on the existence or nonexistence of the link
     */
    private void reweightPeerLinkGraph(SimilarContentViewPeerAlert alert) {
        UniversalId similarPeerId = alert.getSimilarViewPeerId();
        Weight weightToGive = alert.getWeightToGive();
        this.weights.put(similarPeerId, weightToGive);
        
        PeerLinkExistenceRequest request = new PeerLinkExistenceRequest(similarPeerId);
        ActorSelection peerLinker = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerLinker.tell(request, getSelf());
    }
    
    /**
     * Updates the weight if the peer is in existence
     * Otherwise requests a new link with this starting weight be made
     * @param response
     */
    protected void processPeerLinkExistenceResponse(PeerLinkExistenceResponse response) {
        UniversalId peerId = response.getLinkToCheckPeerId();
        boolean linkExists = response.isLinkInExistence();
        if (this.weights.containsKey(peerId)) {
            Weight weight = this.weights.remove(peerId);
            if (linkExists) {
                LocalWeightUpdateRequest weightUpdate = new LocalWeightUpdateRequest(peerId, weight);
                ActorSelection weighter = getContext().actorSelection(ActorPaths.getPathToWeighter(peerId));
                weighter.tell(weightUpdate, getSelf());
            }
            else {
                PeerLinkAddition linkAddition = new PeerLinkAddition(peerId, weight);
                ActorSelection peerLinker = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
                peerLinker.tell(linkAddition, getSelf());
            }
        }
    }
    
    /**
     * Returns a selection of Peer IDs from similarViewPeers who might still have the content
     * They previously viewed the same content that has been deleted locally
     * @param request
     */
    protected void processSimilarContentViewPeerRequest(SimilarContentViewPeerRequest request) {
        Set<UniversalId> similarViewPeers = this.similarViewsPeers.get(request.getContent());
        Iterator<UniversalId> peers = similarViewPeers.iterator();
        while (peers.hasNext()) {
            UniversalId peerId = peers.next();
            SimilarContentViewPeerResponse response = new SimilarContentViewPeerResponse(request.getContent(), peerId);
            getSender().tell(response, getSelf());
        }
    }
}
