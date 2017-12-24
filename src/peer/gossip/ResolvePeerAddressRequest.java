package peer.gossip;

import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

public class ResolvePeerAddressRequest extends PeerToPeerRequest {
    public ResolvePeerAddressRequest(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.ResolvePeerAddressRequest, originalRequester, originalTarget);
    }
}
