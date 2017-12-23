package peer.gossip;

import core.ActorMessage;
import core.ActorMessageType;

/**
 * 
 *
 */
public class GossipInit extends ActorMessage {
    public GossipInit() {
        super(ActorMessageType.GossipInit);
    }
}
