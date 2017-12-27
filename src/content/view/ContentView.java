package content.view;

import content.core.Content;
import peer.core.UniversalId;

/**
 * Information on a viewable piece of Content
 *
 */
public class ContentView {
    private static final double MIN_RATING = 0;
    private static final double MAX_RATING = 5;
    
    private UniversalId viewingPeerId;
    private Content content;
    private int contentLength;
    private double rating;
    private int numberOfViews;
    private double averageViewingTime;
    
    public ContentView(Content content, UniversalId viewingPeerId) {
        this.content = content;
        this.viewingPeerId = viewingPeerId;
        this.contentLength = content.getViewLength();
        this.rating = -1;
        this.numberOfViews = 0;
        this.averageViewingTime = 0;
    }
    
    /**
     * Get Peer ID of the peer who generated this Content View
     * @return
     */
    public UniversalId getViewingPeerId() {
        return this.viewingPeerId;
    }
    
    /**
     * Get Content that was viewed
     * @return content
     */
    public Content getContent() {
        return this.content;
    }
    
    /**
     * Allows peer to set rating for this viewable Content
     * @param rating
     */
    public void setRating(double rating) {
        if (rating > MAX_RATING) {
            this.rating = MAX_RATING;
        }
        else if (rating < MIN_RATING) {
            this.rating = MIN_RATING;
        }
        else {
            this.rating = rating;
        }
    }
    
    /**
     * Record another view of the Content
     * @param viewingTime
     */
    public void recordView(int viewingTime) {
        int viewTime;
        if (viewingTime < 0) {
            viewTime = 0;
        }
        else if (viewingTime > contentLength) {
            viewTime = contentLength;
        }
        else {
            viewTime = viewingTime;
        }
        this.averageViewingTime = 
                (this.averageViewingTime * this.numberOfViews + viewTime) 
                / (this.numberOfViews + 1);
        this.numberOfViews++;
    }
    
    /**
     * Composite Score for viewed Content
     * @return
     */
    public double getScore() {
        double compositeScore = this.averageViewingTime / (double) contentLength;
        if (this.rating > 0) {
            compositeScore *= rating;
        }
        else {
            compositeScore *= (MAX_RATING - MIN_RATING) / (double)2;
        }
        return compositeScore;
    }
}
