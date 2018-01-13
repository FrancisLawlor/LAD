package peer.data.messages;

import content.frame.core.ContentFile;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Saves Content File in Databaser
 *
 */
public class SaveContentFileRequest extends ActorMessage {
    private ContentFile contentFile;
    
    public SaveContentFileRequest(ContentFile contentFile) {
        super(ActorMessageType.SaveContentFileRequest);
        this.contentFile = contentFile;
    }
    
    public ContentFile getContentFile() {
        return this.contentFile;
    }
}
