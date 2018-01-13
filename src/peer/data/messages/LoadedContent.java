package peer.data.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Content loaded from local peer's storage
 *
 */
public class LoadedContent extends ActorMessage {
    private Content content;
    
    public LoadedContent(Content content) {
        super(ActorMessageType.LoadedContent);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
