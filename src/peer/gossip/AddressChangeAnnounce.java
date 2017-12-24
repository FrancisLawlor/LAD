package peer.gossip;

import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

public class AddressChangeAnnounce extends PeerToPeerRequest {
    public AddressChangeAnnounce(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.AddressChangeAnnounce, originalRequester, originalTarget);
    }
}
