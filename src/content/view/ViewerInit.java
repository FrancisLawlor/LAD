package content.view;

import java.util.concurrent.BlockingQueue;

import content.recommend.RecommendationsForUser;
import content.retrieve.RetrievedContent;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;

/**
 * Actor Message that initialises viewer
 *
 */
public class ViewerInit extends ActorMessage {
    private BlockingQueue<RecommendationsForUser> recommendationsQueue;
    private BlockingQueue<RetrievedContent> retrievedContentQueue;
    
    public ViewerInit(BlockingQueue<RecommendationsForUser> recommendationsQueue, BlockingQueue<RetrievedContent> retrievedContentQueue) {
        super(ActorMessageType.ViewerInit);
        this.recommendationsQueue = recommendationsQueue;
        this.retrievedContentQueue = retrievedContentQueue;
    }
    
    public BlockingQueue<RecommendationsForUser> getRecommendationsQueue() {
        return this.recommendationsQueue;
    }
    
    public BlockingQueue<RetrievedContent> getRetrievedContentQueue() {
        return this.retrievedContentQueue;
    }
}
