package peer.core;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import akka.actor.ActorRef;
import content.core.Content;
import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import content.retrieve.LocalRetrieveContentRequest;
import content.retrieve.RetrievedContent;

/**
 * Communication channel between Viewer Actor and UI
 *
 */
public class ViewerToUIChannel {
    private UniversalId peerId;
    private ActorRef viewer;
    private BlockingQueue<RecommendationsForUser> recommendationsQueue;
    private BlockingQueue<RetrievedContent> retrievedContentQueue;
    
    public ViewerToUIChannel(UniversalId peerId, ActorRef viewer, BlockingQueue<RecommendationsForUser> recommendationsQueue, BlockingQueue<RetrievedContent> retrievedContentQueue) {
        this.peerId = peerId;
        this.viewer = viewer;
        this.recommendationsQueue = recommendationsQueue;
        this.retrievedContentQueue = retrievedContentQueue;
    }
    
    public void requestRecommendations() {
        RecommendationsForUserRequest request = new RecommendationsForUserRequest(this.peerId);
        this.viewer.tell(request, null);
    }
    
    public void requestContent(Recommendation recom) {
        LocalRetrieveContentRequest request = new LocalRetrieveContentRequest(this.peerId, recom.getRecommendingPeerId(), recom.getContent());
        this.viewer.tell(request, null);
    }
    
    synchronized public RecommendationsForUser getRecommendations() throws InterruptedException {
        return this.recommendationsQueue.take();
    }
    
    synchronized public RetrievedContent getRetrievedContent() throws InterruptedException {
        return this.retrievedContentQueue.take();
    }
    
    public void recordView(Content content, List<UniversalId> similarPeers) {
        
    }
}
