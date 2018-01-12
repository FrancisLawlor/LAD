package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;
import peer.graph.core.Weight;

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
