package peer.gossip;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

public class OldPeerAddressesRequest extends ActorMessage {
    public OldPeerAddressesRequest() {
        super(ActorMessageType.OldPeerAddressesRequest);
    }
}
