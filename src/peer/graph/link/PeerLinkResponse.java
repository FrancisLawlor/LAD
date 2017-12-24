package peer.graph.link;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;

/**
 * Encapsulates Peer ID from a theoretical link between it and this peer
 *
 */
public class PeerLinkResponse extends ActorMessage {
    private UniversalId peerId;
    
    public PeerLinkResponse(UniversalId peerId) {
        super(ActorMessageType.PeerLinkResponse);
        this.peerId = peerId;
    }
    
    public UniversalId getPeerId() {
        return this.peerId;
    }
}
