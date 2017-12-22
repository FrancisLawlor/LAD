package content.retrieve;

import content.impl.Content;
import core.ActorMessageType;
import core.PeerToPeerRequest;
import core.UniversalId;

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
