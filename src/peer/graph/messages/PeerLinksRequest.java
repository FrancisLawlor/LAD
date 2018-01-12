package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Actor message requesting Peer IDs from 
 * theoretical links between this peer and others
 *
 */
public class PeerLinksRequest extends ActorMessage {
    public PeerLinksRequest() {
        super(ActorMessageType.PeerLinksRequest);
    }
}
