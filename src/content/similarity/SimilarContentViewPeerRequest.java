package content.similarity;

import content.core.Content;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;

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
