package peer.data;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.graph.distributedmap.PeerWeightedLink;

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
