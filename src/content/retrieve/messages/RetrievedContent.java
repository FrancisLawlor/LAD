package content.retrieve.messages;

import content.frame.core.Content;
import content.retrieve.core.TransferInfo;
import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerRequest;

/**
 * Content retrieved for the original Requester from the original Target
 *
 */
public class RetrievedContent extends PeerToPeerRequest {
    private Content content;
    private TransferInfo transferInfo;
    
    public RetrievedContent(UniversalId originalRequester, UniversalId originalTarget, Content content, TransferInfo transferInfo) {
        super(ActorMessageType.RetrievedContent, originalRequester, originalTarget);
        this.content = content;
        this.transferInfo = transferInfo;
    }
    
    public Content getContent() {
        return this.content;
    }
    
    public TransferInfo getTransferInfo() {
        return this.transferInfo;
    }
}
