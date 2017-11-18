package peer.graph.link;

import core.ActorMessage;

/**
 * Encapsulates Peer ID from a theoretical link between it and this peer
 *
 */
public class PeerLinkResponse extends ActorMessage {
    private String peerId;
    
    public PeerLinkResponse(String peerId) {
        this.peerId = peerId;
    }
    
    public String getPeerId() {
        return this.peerId;
    }
}
