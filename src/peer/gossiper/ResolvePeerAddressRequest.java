package peer.gossiper;

import core.RequestCommunication;
import core.UniversalId;

public class ResolvePeerAddressRequest extends RequestCommunication {
    public ResolvePeerAddressRequest(UniversalId originalRequester, UniversalId originalTarget) {
        super(originalRequester, originalTarget);
    }
}
