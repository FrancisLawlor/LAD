package peer.data.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

public class LocalSavedContentResponse extends ActorMessage {
    private Content savedContent;
    
    public LocalSavedContentResponse(Content savedContent) {
        super(ActorMessageType.LocalSavedContentResponse);
        this.savedContent = savedContent;
    }
    
    public Content getContent() {
        return this.savedContent;
    }
}
