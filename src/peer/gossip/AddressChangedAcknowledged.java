package peer.gossip;

import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

public class AddressChangedAcknowledged extends PeerToPeerRequest {
    public AddressChangedAcknowledged(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.AddressChangedAcknowledged, originalRequester, originalTarget);
    }
}
