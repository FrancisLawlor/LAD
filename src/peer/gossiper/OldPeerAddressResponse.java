package peer.gossiper;

import core.ActorMessage;
import core.ActorMessageType;

public class OldPeerAddressResponse extends ActorMessage {
    public OldPeerAddressResponse() {
        super(ActorMessageType.OldPeerAddressResponse);
    }
}
