package peer.frame.messages;

import peer.frame.core.ActorMessageType;

/**
 * Superclass for all Actor Messages
 *
 */
public class ActorMessage {
    private ActorMessageType type;
    
    public ActorMessage(ActorMessageType type) {
        this.type = type;
    }
    
    public ActorMessageType getType() {
        return this.type;
    }
}
