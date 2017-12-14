package peer.gossiper;

import core.RequestCommunication;
import core.UniversalId;

public class AddressChangeAnnounce extends RequestCommunication {
    public AddressChangeAnnounce(UniversalId originalRequester, UniversalId originalTarget) {
        super(originalRequester, originalTarget);
    }
}
