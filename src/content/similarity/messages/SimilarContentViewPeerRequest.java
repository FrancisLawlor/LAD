package content.similarity.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Requests gossip on what peer might have the file for this content
 *
 */
public class SimilarContentViewPeerRequest extends ActorMessage {
    private Content content;
    
    public SimilarContentViewPeerRequest(Content content) {
        super(ActorMessageType.SimilarContentViewPeerRequest);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
