package peer.gossiper;

import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

public class AddressChangedAcknowledged extends RequestCommunication {
    public AddressChangedAcknowledged(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.AddressChangedAcknowledged, originalRequester, originalTarget);
    }
}
