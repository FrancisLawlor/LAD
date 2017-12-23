package peer.gossip;

import core.ActorMessageType;
import core.PeerToPeerRequest;
import core.UniversalId;

public class NewAddressForPeer extends PeerToPeerRequest {
    public NewAddressForPeer(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.NewAddressForPeer, originalRequester, originalTarget);
    }
}
