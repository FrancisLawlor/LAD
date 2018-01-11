package peer.data;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.graph.distributedmap.PeerWeightedLink;

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
