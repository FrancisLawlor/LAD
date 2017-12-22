package content.retrieve;

import content.impl.Content;
import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

/**
 * Content retrieved for the original Requester from the original Target
 *
 */
public class RetrievedContent extends RequestCommunication {
    private Content content;
    
    public RetrievedContent(UniversalId originalRequester, UniversalId originalTarget, Content content) {
        super(ActorMessageType.RetrievedContent, originalRequester, originalTarget);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
