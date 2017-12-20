package peer.gossiper;

import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

public class ResolvePeerAddressRequest extends RequestCommunication {
    public ResolvePeerAddressRequest(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.ResolvePeerAddressRequest, originalRequester, originalTarget);
    }
}
