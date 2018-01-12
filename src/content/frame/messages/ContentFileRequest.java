package content.frame.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

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
