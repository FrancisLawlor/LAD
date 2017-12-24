package peer.gossip;

import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

public class NewAddressForPeer extends PeerToPeerRequest {
    public NewAddressForPeer(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.NewAddressForPeer, originalRequester, originalTarget);
    }
}
