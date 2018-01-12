package peer.graph.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;

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
