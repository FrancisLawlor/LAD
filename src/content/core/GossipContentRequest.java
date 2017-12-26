package content.core;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Requests gossip on what peer might have the file for this content
 *
 */
public class GossipContentRequest extends ActorMessage {
    private Content content;
    
    public GossipContentRequest(Content content) {
        super(ActorMessageType.GossipContentRequest);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
