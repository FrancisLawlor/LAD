package peer.gossip;

import core.ActorMessage;
import core.ActorMessageType;

public class OldPeerAddressesRequest extends ActorMessage {
    public OldPeerAddressesRequest() {
        super(ActorMessageType.OldPeerAddressesRequest);
    }
}
