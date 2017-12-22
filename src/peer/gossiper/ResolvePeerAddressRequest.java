package peer.gossiper;

import core.ActorMessageType;
import core.PeerToPeerRequest;
import core.UniversalId;

public class ResolvePeerAddressRequest extends PeerToPeerRequest {
    public ResolvePeerAddressRequest(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.ResolvePeerAddressRequest, originalRequester, originalTarget);
    }
}
