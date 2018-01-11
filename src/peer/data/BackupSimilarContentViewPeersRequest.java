package peer.data;

import content.similarity.SimilarContentViewPeers;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;

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
