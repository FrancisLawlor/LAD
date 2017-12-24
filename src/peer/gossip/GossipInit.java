package peer.gossip;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * 
 *
 */
public class GossipInit extends ActorMessage {
    public GossipInit() {
        super(ActorMessageType.GossipInit);
    }
}
