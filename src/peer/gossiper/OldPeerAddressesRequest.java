package peer.gossiper;

import core.ActorMessage;
import core.ActorMessageType;

public class OldPeerAddressesRequest extends ActorMessage {
    public OldPeerAddressesRequest() {
        super(ActorMessageType.OldPeerAddressesRequest);
    }
}
