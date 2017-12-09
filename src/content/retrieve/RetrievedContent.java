package content.retrieve;

import core.ActorMessage;

public class RetrievedContent extends ActorMessage {
    private String url;
    
    public RetrievedContent(String url) {
        
    }
    
    public String getURLString() {
        return this.url;
    }
}
