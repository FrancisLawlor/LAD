package peer.gossip;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

public class OldPeerAddressResponse extends ActorMessage {
    public OldPeerAddressResponse() {
        super(ActorMessageType.OldPeerAddressResponse);
    }
}
