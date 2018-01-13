package content.view.messages;

import java.util.concurrent.BlockingQueue;

import content.recommend.messages.RecommendationsForUser;
import content.retrieve.messages.RetrievedContent;
import peer.data.messages.LoadedContent;
import peer.data.messages.LocalSavedContentResponse;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Actor Message that initialises viewer
 *
 */
public class ViewerInit extends ActorMessage {
    private BlockingQueue<RecommendationsForUser> recommendationsQueue;
    private BlockingQueue<RetrievedContent> retrievedContentQueue;
    private BlockingQueue<LocalSavedContentResponse> savedContentQueue;
    private BlockingQueue<LoadedContent> loadedContentQueue;
    
    public ViewerInit(BlockingQueue<RecommendationsForUser> recommendationsQueue, BlockingQueue<RetrievedContent> retrievedContentQueue,
            BlockingQueue<LocalSavedContentResponse> savedContentQueue, BlockingQueue<LoadedContent> loadedContentQueue) {
        super(ActorMessageType.ViewerInit);
        this.recommendationsQueue = recommendationsQueue;
        this.retrievedContentQueue = retrievedContentQueue;
        this.savedContentQueue = savedContentQueue;
        this.loadedContentQueue = loadedContentQueue;
    }
    
    public BlockingQueue<RecommendationsForUser> getRecommendationsQueue() {
        return this.recommendationsQueue;
    }
    
    public BlockingQueue<RetrievedContent> getRetrievedContentQueue() {
        return this.retrievedContentQueue;
    }
    
    public BlockingQueue<LocalSavedContentResponse> getSavedContentQueue() {
        return this.savedContentQueue;
    }
    
    public BlockingQueue<LoadedContent> getLoadedContentQueue() {
        return this.loadedContentQueue;
    }
}
