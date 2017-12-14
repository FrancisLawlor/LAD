package peer.graph.link;

import core.ActorMessage;
import core.UniversalId;

/**
 * Encapsulates Peer ID from a theoretical link between it and this peer
 *
 */
public class PeerLinkResponse extends ActorMessage {
    private UniversalId peerId;
    
    public PeerLinkResponse(UniversalId peerId) {
        this.peerId = peerId;
    }
    
    public UniversalId getPeerId() {
        return this.peerId;
    }
}
