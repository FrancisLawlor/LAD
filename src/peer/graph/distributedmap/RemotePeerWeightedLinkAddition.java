package peer.graph.distributedmap;

import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;
import peer.graph.weight.Weight;

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