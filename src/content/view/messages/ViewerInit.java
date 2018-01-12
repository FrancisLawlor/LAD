package content.view.messages;

import java.util.concurrent.BlockingQueue;

import content.recommend.messages.RecommendationsForUser;
import content.retrieve.messages.RetrievedContent;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

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
