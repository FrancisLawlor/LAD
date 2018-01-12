package content.retrieve.messages;

/**
 * Signals to the Transferer it will be receiving
 * Contains the Content info to go along with the raw bytes in the ContentFile 
 *
 */
public class TransferReceiveStart {
    private RetrievedContent retrievedContent;
    
    public TransferReceiveStart(RetrievedContent retrievedContent) {
        this.retrievedContent = retrievedContent;
    }
    
    public RetrievedContent getRetrievedContent() {
        return this.retrievedContent;
    }
}
