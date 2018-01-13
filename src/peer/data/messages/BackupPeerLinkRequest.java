package peer.data.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;
import peer.graph.core.PeerWeightedLink;

/**
 * Requests this Peer Link be backed up to the databaser's backup
 *
 */
public class BackupPeerLinkRequest extends ActorMessage {
    private PeerWeightedLink peerWeightedLink;
    
    public BackupPeerLinkRequest(PeerWeightedLink peerWeightedLink) {
        super(ActorMessageType.BackupPeerLinkRequest);
        this.peerWeightedLink = peerWeightedLink;
    }
    
    public PeerWeightedLink getPeerWeightedLink() {
        return this.peerWeightedLink;
    }
}
