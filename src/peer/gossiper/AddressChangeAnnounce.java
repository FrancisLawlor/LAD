package peer.gossiper;

import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

public class AddressChangeAnnounce extends RequestCommunication {
    public AddressChangeAnnounce(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.AddressChangeAnnounce, originalRequester, originalTarget);
    }
}
