package peer.frame.core;

import java.util.concurrent.BlockingQueue;

import akka.actor.ActorRef;
import content.frame.core.Content;
import content.recommend.core.Recommendation;
import content.recommend.messages.RecommendationsForUser;
import content.recommend.messages.RecommendationsForUserRequest;
import content.retrieve.messages.LocalRetrieveContentRequest;
import content.retrieve.messages.RetrievedContent;
import content.view.core.ContentView;
import content.view.core.Rating;
import content.view.core.ViewingTime;
import content.view.messages.RecordContentView;
import peer.frame.exceptions.InvalidSwitchCaseException;
import peer.frame.exceptions.UncreatedContentViewException;
import peer.frame.exceptions.UnrecordedContentViewException;

/**
 * Communication channel between Viewer Actor and UI
 *
 */
public class ViewerToUIChannel {
    private UniversalId peerId;
    private ActorRef viewer;
    private BlockingQueue<RecommendationsForUser> recommendationsQueue;
    private BlockingQueue<RetrievedContent> retrievedContentQueue;
    private ContentView contentView;
    
    public ViewerToUIChannel(UniversalId peerId, ActorRef viewer, BlockingQueue<RecommendationsForUser> recommendationsQueue, BlockingQueue<RetrievedContent> retrievedContentQueue) {
        this.peerId = peerId;
        this.viewer = viewer;
        this.recommendationsQueue = recommendationsQueue;
        this.retrievedContentQueue = retrievedContentQueue;
        this.contentView = null;
    }
    
    synchronized public void requestRecommendations() {
        RecommendationsForUserRequest request = new RecommendationsForUserRequest(this.peerId);
        this.viewer.tell(request, ActorRef.noSender());
    }
    
    synchronized public void requestContent(Recommendation recom) {
        LocalRetrieveContentRequest request = new LocalRetrieveContentRequest(this.peerId, recom.getRecommendingPeerId(), recom.getContent());
        this.viewer.tell(request, ActorRef.noSender());
    }
    
    synchronized public RecommendationsForUser getRecommendations() throws InterruptedException {
        return this.recommendationsQueue.take();
    }
    
    synchronized public RetrievedContent getRetrievedContent() throws InterruptedException {
        return this.retrievedContentQueue.take();
    }
    
    synchronized public void createNewContentView(Content content) {
        this.threadSafeMutateContentViewField(ContentViewMutate.Create, content, null, null);
    }
    
    synchronized public void recordContentViewInfo(ViewingTime viewingTime, Rating rating) {
        this.threadSafeMutateContentViewField(ContentViewMutate.Record, null, viewingTime, rating);
    }
    
    private enum ContentViewMutate { Create, Record; }
    
    synchronized private void threadSafeMutateContentViewField(ContentViewMutate mutate, Content content, ViewingTime viewingTime, Rating rating) {
        switch (mutate) {
        case Create:
            if (this.contentView == null) {
                this.contentView = new ContentView(content, this.peerId);
            }
            else throw new UnrecordedContentViewException();
            break;
        case Record:
            if (this.contentView != null) {
                this.contentView.recordView(viewingTime);
                if (rating != null) {
                    this.contentView.setRating(rating);
                }
                RecordContentView recordContentView = new RecordContentView(this.contentView);
                this.viewer.tell(recordContentView, ActorRef.noSender());
                this.contentView = null;
            }
            else throw new UncreatedContentViewException();
            break;
        default:
            throw new InvalidSwitchCaseException();
        }
    }
}
