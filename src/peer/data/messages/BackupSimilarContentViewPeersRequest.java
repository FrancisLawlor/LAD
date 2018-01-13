package peer.data.messages;

import content.similarity.core.SimilarContentViewPeers;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Requests this SimilarContentViewPeers is backed up to the Databaser's backup
 *
 */
public class BackupSimilarContentViewPeersRequest extends ActorMessage {
    private SimilarContentViewPeers similarContentViewPeers;
    
    public BackupSimilarContentViewPeersRequest(SimilarContentViewPeers similarContentViewPeers) {
        super(ActorMessageType.BackupSimilarContentViewPeersRequest);
        this.similarContentViewPeers = similarContentViewPeers;
    }
    
    public SimilarContentViewPeers getSimilarContentViewPeers() {
        return this.similarContentViewPeers;
    }
}
