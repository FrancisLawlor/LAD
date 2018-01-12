package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;

/**
 * Confirms or denies existence of a stored link between this peer and a specific other
 *
 */
public class PeerLinkExistenceResponse extends ActorMessage {
    private UniversalId linkToCheckPeerId;
    private boolean existenceOfLink;
    
    public PeerLinkExistenceResponse(UniversalId linkToCheckPeerId, boolean existenceOfLink) {
        super(ActorMessageType.PeerLinkExistenceResponse);
        this.linkToCheckPeerId = linkToCheckPeerId;
        this.existenceOfLink = existenceOfLink;
    }
    
    public UniversalId getLinkToCheckPeerId() {
        return this.linkToCheckPeerId;
    }
    
    public boolean isLinkInExistence() {
        return this.existenceOfLink;
    }
}
