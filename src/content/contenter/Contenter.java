package content.contenter;

import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorSelection;
import core.ActorPaths;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.graph.link.PeerLinkAddition;
import peer.graph.link.PeerLinkExistenceRequest;
import peer.graph.link.PeerLinkExistenceResponse;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.Weight;

/**
 * Updates theoretical graph based on Header data from Retrieved Content
 * Updates PeerLinker and Weighters to increase weight of links between peers that viewed similar content to this peer
 *
 */
public class Contenter extends PeerToPeerActor {
    private Map<UniversalId, Weight> weights;
    
    public Contenter() {
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
        else if (message instanceof PeerSimilarViewAlert) {
            PeerSimilarViewAlert alert = (PeerSimilarViewAlert) message;
            this.processPeerSimilarViewAlert(alert);
        }
        else if (message instanceof PeerLinkExistenceResponse) {
            PeerLinkExistenceResponse response = (PeerLinkExistenceResponse) message;
            this.processPeerLinkExistenceResponse(response);
        }
    }
    
    /**
     * Temporarily stores this peer's similarity weight for this similar view
     * Asks if peerLinker for the theoretical peer weight graph already has a link or not
     * How to add this weight to the graph depends on the existence or nonexistence of the link
     * 
     * @param alert
     */
    protected void processPeerSimilarViewAlert(PeerSimilarViewAlert alert) {
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
            Weight weight = this.weights.get(peerId);
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
}
