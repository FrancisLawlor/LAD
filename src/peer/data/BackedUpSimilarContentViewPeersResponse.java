package peer.data;

import content.similarity.SimilarContentViewPeers;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;

public class BackedUpSimilarContentViewPeersResponse extends ActorMessage {
    private SimilarContentViewPeers similarContentViewPeers;
    
    public BackedUpSimilarContentViewPeersResponse(SimilarContentViewPeers similarContentViewPeers) {
        super(ActorMessageType.BackedUpSimilarContentViewPeersResponse);
        this.similarContentViewPeers = similarContentViewPeers;
    }
    
    public SimilarContentViewPeers getSimilarContentViewPeers() {
        return this.similarContentViewPeers;
    }
}
