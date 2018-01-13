package peer.frame.core;

import java.util.concurrent.BlockingQueue;

import akka.actor.ActorRef;
import content.frame.core.Content;
import content.frame.core.ContentFile;
import content.recommend.core.Recommendation;
import content.recommend.messages.RecommendationsForUser;
import content.recommend.messages.RecommendationsForUserRequest;
import content.retrieve.messages.LocalRetrieveContentRequest;
import content.retrieve.messages.RetrievedContent;
import content.view.core.ContentView;
import content.view.core.Rating;
import content.view.core.ViewingTime;
import content.view.messages.RecordContentView;
import peer.data.messages.LoadSavedContentFileRequest;
import peer.data.messages.LoadedContent;
import peer.data.messages.LocalSavedContentRequest;
import peer.data.messages.LocalSavedContentResponse;
import peer.data.messages.SaveContentFileRequest;
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
    private BlockingQueue<LocalSavedContentResponse> savedContentQueue;
    private BlockingQueue<LoadedContent> loadedContentQueue;
    private ContentView contentView;
    
    public ViewerToUIChannel(UniversalId peerId, ActorRef viewer,
            BlockingQueue<RecommendationsForUser> recommendationsQueue, 
            BlockingQueue<RetrievedContent> retrievedContentQueue,
            BlockingQueue<LocalSavedContentResponse> savedContentQueue, 
            BlockingQueue<LoadedContent> loadedContentQueue) {
        this.peerId = peerId;
        this.viewer = viewer;
        this.recommendationsQueue = recommendationsQueue;
        this.retrievedContentQueue = retrievedContentQueue;
        this.savedContentQueue = savedContentQueue;
        this.loadedContentQueue = loadedContentQueue;
        this.contentView = null;
    }
    
    /**
     * Request Viewer retrieves Recommendations
     */
    synchronized public void requestRecommendations() {
        RecommendationsForUserRequest request = new RecommendationsForUserRequest(this.peerId);
        this.viewer.tell(request, ActorRef.noSender());
    }
    
    /**
     * Request Viewer retrieves content associated with this Recommendation
     * @param recom
     */
    synchronized public void requestContent(Recommendation recom) {
        LocalRetrieveContentRequest request = new LocalRetrieveContentRequest(this.peerId, recom.getRecommendingPeerId(), recom.getContent());
        this.viewer.tell(request, ActorRef.noSender());
    }
    
    /**
     * Blocks on queue waiting for RecommendationsForUser
     * @return
     * @throws InterruptedException
     */
    synchronized public RecommendationsForUser getRecommendations() throws InterruptedException {
        return this.recommendationsQueue.take();
    }
    
    /**
     * Blocks on queue waiting for RetrievedContent
     * @return
     * @throws InterruptedException
     */
    synchronized public RetrievedContent getRetrievedContent() throws InterruptedException {
        return this.retrievedContentQueue.take();
    }
    
    /**
     * Create a new ContentView with no ViewingTime or Rating recorded in it yet
     * @param content
     */
    synchronized public void createNewContentView(Content content) {
        this.threadSafeMutateContentViewField(ContentViewMutate.Create, content, null, null);
    }
    
    /**
     * Record ViewingTime and an optional Rating
     * @param viewingTime
     * @param rating
     */
    synchronized public void recordContentViewInfo(ViewingTime viewingTime, Rating rating) {
        this.threadSafeMutateContentViewField(ContentViewMutate.Record, null, viewingTime, rating);
    }
    
    private enum ContentViewMutate { Create, Record; }
    
    /**
     * Private helper method that is synchronized so the Content View field can only be created or modified in a mutually exclusive way
     * @param mutate
     * @param content
     * @param viewingTime
     * @param rating
     */
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
    
    /**
     * Ask Viewer to pass on a user's own uploaded file to the databaser for storage
     * @param contentFile
     */
    synchronized public void saveContentFile(ContentFile contentFile) {
        SaveContentFileRequest request = new SaveContentFileRequest(contentFile);
        this.viewer.tell(request, ActorRef.noSender());
    }
    
    /**
     * Ask the Viewer to get Content objects describing the ContentFiles stored on this peer
     */
    synchronized public void requestSavedContent() {
        LocalSavedContentRequest request = new LocalSavedContentRequest();
        this.viewer.tell(request, ActorRef.noSender());
    }
    
    /**
     * Blocks on queue until Viewer fills with Saved Content
     * @return
     * @throws InterruptedException
     */
    synchronized public LocalSavedContentResponse getSavedContent() throws InterruptedException {
        return this.savedContentQueue.take();
    }
    
    /**
     * Requests RetrievedContent from saved content stored on this peer
     * @param content
     */
    synchronized public void requestRetrievalOfSavedContentFile(Content content) {
        LoadSavedContentFileRequest request = new LoadSavedContentFileRequest(content);
        this.viewer.tell(request, ActorRef.noSender());
    }
    
    /**
     * Blocks on queue until Viewer fills with LoadedContent
     * @return
     * @throws InterruptedException
     */
    synchronized public LoadedContent getLoadedContent() throws InterruptedException {
        return this.loadedContentQueue.take();
    }
}
