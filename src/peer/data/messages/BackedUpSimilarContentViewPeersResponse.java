package peer.data.messages;

import content.similarity.core.SimilarContentViewPeers;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Returns a SimilarContentViewPeers from the Databaser's backup
 *
 */
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
