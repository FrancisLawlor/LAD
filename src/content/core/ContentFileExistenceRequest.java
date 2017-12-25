package content.core;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Asks if a file for this Content exists locally on the peer's storage
 *
 */
public class ContentFileExistenceRequest extends ActorMessage {
    private Content content;
    
    public ContentFileExistenceRequest(Content content) {
        super(ActorMessageType.ContentFileExistenceRequest);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
