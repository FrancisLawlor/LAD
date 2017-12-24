package content.retrieve;

import content.core.Content;
import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

/**
 * Content retrieved for the original Requester from the original Target
 *
 */
public class RetrievedContent extends PeerToPeerRequest {
    private Content content;
    
    public RetrievedContent(UniversalId originalRequester, UniversalId originalTarget, Content content) {
        super(ActorMessageType.RetrievedContent, originalRequester, originalTarget);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
