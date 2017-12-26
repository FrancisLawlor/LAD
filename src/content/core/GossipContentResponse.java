package content.core;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;

/**
 * Suggests this peer might have the file for this content
 *
 */
public class GossipContentResponse extends ActorMessage {
    private Content content;
    private UniversalId peerId;
    
    public GossipContentResponse(Content content, UniversalId peerId) {
        super(ActorMessageType.GossipContentResponse);
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
