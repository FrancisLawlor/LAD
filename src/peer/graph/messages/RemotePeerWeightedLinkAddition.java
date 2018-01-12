package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerRequest;
import peer.graph.core.Weight;

/**
 * For creating weighted link on other peer's end
 * Vital to keep link consistent in the first place
 *
 */
public class RemotePeerWeightedLinkAddition extends PeerToPeerRequest {
    private Weight linkWeight;
    
    public RemotePeerWeightedLinkAddition(UniversalId originalRequester, UniversalId originalTarget, Weight linkWeight) {
        super(ActorMessageType.RemotePeerWeightedLinkAddition, originalRequester, originalTarget);
        this.linkWeight = linkWeight;
    }
    
    public UniversalId getLinkPeerId() {
        return super.getOriginalRequester();
    }
    
    public Weight getLinkWeight() {
        return this.linkWeight;
    }
}