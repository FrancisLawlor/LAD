package peer.data.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Requests all backed up Peer Links from the databaser's backup
 *
 */
public class BackedUpPeerLinksRequest extends ActorMessage {
    public BackedUpPeerLinksRequest() {
        super(ActorMessageType.BackedUpPeerLinksRequest);
    }
}
