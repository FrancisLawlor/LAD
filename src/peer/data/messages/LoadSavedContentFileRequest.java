package peer.data.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Requests a content file that has been saved locally
 *
 */
public class LoadSavedContentFileRequest extends ActorMessage {
    private Content content;
    
    public LoadSavedContentFileRequest(Content content) {
        super(ActorMessageType.LocalSavedContentFileRequest);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
