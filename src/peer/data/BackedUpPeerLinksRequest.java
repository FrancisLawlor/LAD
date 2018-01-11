package peer.data;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

public class BackedUpPeerLinksRequest extends ActorMessage {
    public BackedUpPeerLinksRequest() {
        super(ActorMessageType.BackedUpPeerLinksRequest);
    }
}
