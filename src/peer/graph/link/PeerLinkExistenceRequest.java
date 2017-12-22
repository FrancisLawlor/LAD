package peer.graph.link;

import core.ActorMessage;
import core.ActorMessageType;
import core.UniversalId;

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
