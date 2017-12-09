package content.retrieve;

import content.content.Content;
import core.ActorMessage;
import core.UniversalId;

public class LocalRetrieveContentRequest extends ActorMessage {
    public LocalRetrieveContentRequest(Content content) {
        
    }
    
    public UniversalId getRecipient() {
        
        return null;
    }
}
