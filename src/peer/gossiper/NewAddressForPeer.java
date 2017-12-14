package peer.gossiper;

import core.RequestCommunication;
import core.UniversalId;

public class NewAddressForPeer extends RequestCommunication {
    public NewAddressForPeer(UniversalId originalRequester, UniversalId originalTarget) {
        super(originalRequester, originalTarget);
    }
}
