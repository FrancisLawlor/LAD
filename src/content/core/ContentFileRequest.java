package content.core;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Requests the file associated with this content from this peer's local storage
 *
 */
public class ContentFileRequest extends ActorMessage {
    private Content content;
    
    public ContentFileRequest(Content content) {
        super(ActorMessageType.ContentFileRequest);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
