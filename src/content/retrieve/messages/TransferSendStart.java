package content.retrieve.messages;

import content.frame.core.ContentFile;

/**
 * Signals to the Transferer it will be sending
 *
 */
public class TransferSendStart {
    private ContentFile contentFile;
    
    public TransferSendStart(ContentFile contentFile) {
        this.contentFile = contentFile;
    }
    
    public ContentFile getContentFile() {
        return this.contentFile;
    }
}

