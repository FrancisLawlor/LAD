package content.frame.messages;

import content.frame.core.ContentFile;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Returns the Content File from storage in response to a request
 *
 */
public class ContentFileResponse extends ActorMessage {
    private ContentFile contentFile;
    
    public ContentFileResponse(ContentFile contentFile) {
        super(ActorMessageType.ContentFileResponse);
        this.contentFile = contentFile;
    }
    
    public ContentFile getContentFile() {
        return this.contentFile;
    }
}
