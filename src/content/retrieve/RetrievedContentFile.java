package content.retrieve;

import content.core.ContentFile;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Actor Message that passes back Retrieved Content File
 *
 */
public class RetrievedContentFile extends ActorMessage {
    private RetrievedContent retrievedContent;
    private ContentFile contentFile;
    
    public RetrievedContentFile(RetrievedContent retrievedContent, ContentFile contentFile) {
        super(ActorMessageType.RetrievedContentFile);
        this.retrievedContent = retrievedContent;
        this.contentFile = contentFile;
    }
    
    public RetrievedContent getRetrievedContent() {
        return this.retrievedContent;
    }
    
    public ContentFile getContentFile() {
        return this.contentFile;
    }
}
