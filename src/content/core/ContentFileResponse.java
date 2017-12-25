package content.core;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

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
