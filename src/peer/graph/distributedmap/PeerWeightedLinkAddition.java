package peer.graph.distributedmap;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;
import peer.graph.weight.Weight;

/**
 * Add a weighted link to the PeerWeightedLinkor
 *
 */
public class PeerWeightedLinkAddition extends ActorMessage {
    private UniversalId linkPeerId;
    private Weight linkWeight;
    
    public PeerWeightedLinkAddition(UniversalId linkPeerId, Weight linkWeight) {
        super(ActorMessageType.PeerWeightedLinkAddition);
        this.linkPeerId = linkPeerId;
        this.linkWeight = linkWeight;
    }
    
    public UniversalId getLinkPeerId() {
        return this.linkPeerId;
    }
    
    public Weight getLinkWeight() {
        return this.linkWeight;
    }
}
