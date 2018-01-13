package peer.data.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;
import peer.graph.core.PeerWeightedLink;

/**
 * Returns a backed up Peer Link from the databaser's backup
 *
 */
public class BackedUpPeerLinkResponse extends ActorMessage {
    private PeerWeightedLink peerWeightedLink;
    
    public BackedUpPeerLinkResponse(PeerWeightedLink peerWeightedLink) {
        super(ActorMessageType.BackedUpPeerLinkResponse);
        this.peerWeightedLink = peerWeightedLink;
    }
    
    public PeerWeightedLink getPeerWeightedLink() {
        return this.peerWeightedLink;
    }
}
