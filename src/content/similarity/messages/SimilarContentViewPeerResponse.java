package content.similarity.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;

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
