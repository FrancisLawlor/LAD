package peer.gossip;

import core.ActorMessageType;
import core.PeerToPeerRequest;
import core.UniversalId;

public class AddressChangedAcknowledged extends PeerToPeerRequest {
    public AddressChangedAcknowledged(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.AddressChangedAcknowledged, originalRequester, originalTarget);
    }
}
