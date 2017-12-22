package peer.gossiper;

import core.ActorMessageType;
import core.PeerToPeerRequest;
import core.UniversalId;

public class AddressChangeAnnounce extends PeerToPeerRequest {
    public AddressChangeAnnounce(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.AddressChangeAnnounce, originalRequester, originalTarget);
    }
}
