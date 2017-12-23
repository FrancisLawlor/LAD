package content.view;

import java.util.concurrent.BlockingQueue;

import content.recommend.RecommendationsForUser;
import core.ActorMessage;
import core.ActorMessageType;

/**
 * Actor Message that initialises viewer
 *
 */
public class ViewerInit extends ActorMessage {
    private BlockingQueue<RecommendationsForUser> recommendationsQueue;
    
    public ViewerInit(BlockingQueue<RecommendationsForUser> recommendationsQueue) {
        super(ActorMessageType.ViewerInit);
        this.recommendationsQueue = recommendationsQueue;
    }
    
    public BlockingQueue<RecommendationsForUser> getRecommendationsQueue() {
        return this.recommendationsQueue;
    }
}
