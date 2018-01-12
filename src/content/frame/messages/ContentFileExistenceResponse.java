package content.frame.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Response tells if a piece of content has a file stored locally on this peer
 *
 */
public class ContentFileExistenceResponse extends ActorMessage {
    private Content content;
    private boolean exists;
    
    public ContentFileExistenceResponse(Content content, boolean exists) {
        super(ActorMessageType.ContentFileExistenceResponse);
        this.content = content;
        this.exists = exists;
    }
    
    public Content getContent() {
        return this.content;
    }
    
    public boolean hasContentFile() {
        return this.exists;
    }
    
}
