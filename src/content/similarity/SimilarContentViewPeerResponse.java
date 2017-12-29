package content.similarity;

import content.core.Content;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;

/**
 * Suggests this peer might have the file for this content
 *
 */
public class SimilarContentViewPeerResponse extends ActorMessage {
    private Content content;
    private UniversalId peerId;
    
    public SimilarContentViewPeerResponse(Content content, UniversalId peerId) {
        super(ActorMessageType.SimilarContentViewPeerResponse);
        this.content = content;
        this.peerId = peerId;
    }
    
    public Content getContent() {
        return this.content;
    }
    
    public UniversalId getPeerId() {
        return this.peerId;
    }
}
