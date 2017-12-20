package peer.graph.link;

import core.ActorMessage;
import core.ActorMessageType;

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
