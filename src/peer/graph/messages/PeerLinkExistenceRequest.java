package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;

/**
 * Requests link between this peer and another specific peer
 * Used to check if a link exists between them
 *
 */
public class PeerLinkExistenceRequest extends ActorMessage {
    private UniversalId linkToCheckPeerId;
    
    public PeerLinkExistenceRequest(UniversalId linkToCheckPeerId) {
        super(ActorMessageType.PeerLinkExistenceRequest);
        this.linkToCheckPeerId = linkToCheckPeerId;
    }
    
    public UniversalId getLinkToCheckPeerId() {
        return this.linkToCheckPeerId;
    }
}
