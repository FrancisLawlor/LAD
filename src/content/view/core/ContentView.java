package content.view.core;

import content.frame.core.Content;
import peer.frame.core.UniversalId;

/**
 * Information on a viewable piece of Content
 *
 */
public class ContentView {
    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 5;
    private static final int RANGE = MAX_RATING - MIN_RATING;
    
    private UniversalId viewingPeerId;
    private Content content;
    private double normalisedRating;
    private int ratingCount;
    private int numberOfViews;
    private double averageViewingTime;
    
    public ContentView(Content content, UniversalId viewingPeerId) {
        this.content = content;
        this.viewingPeerId = viewingPeerId;
        this.normalisedRating = -1;
        this.ratingCount = 0;
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
    public void setRating(Rating rating) {
        double boundedRating = this.boundedRating(rating);
        this.normalisedRating = this.normaliseRating(boundedRating);
        this.ratingCount++;
    }
    
    /**
     * Bounds the rating between MIN_RATING and MAX_RATING
     * @return
     */
    private double boundedRating(Rating rating) {
        double boundedRating;
        if (rating.getRating() > MAX_RATING) {
            boundedRating = MAX_RATING;
        }
        else if (rating.getRating() < MIN_RATING) {
            boundedRating = MIN_RATING;
        }
        else {
            boundedRating = rating.getRating();
        }
        return boundedRating;
    }
    
    /**
     * Normalise the rating to the range
     * @param boundedRating
     * @return
     */
    private double normaliseRating(double boundedRating) {
        return (boundedRating - MIN_RATING) / RANGE;
    }
    
    /**
     * Record another view of the Content
     * @param viewingTime
     */
    public void recordView(ViewingTime viewingTime) {
        int viewTime = this.boundViewTime(viewingTime);
        double totalViewTime = this.averageViewingTime * this.numberOfViews;
        this.numberOfViews++;
        this.averageViewingTime = (totalViewTime + viewTime) / this.numberOfViews;
    }
    
    /**
     * Bounds the view time between 0 and the content's viewable length
     * @param viewTime
     * @return
     */
    private int boundViewTime(ViewingTime viewingTime) {
        int viewTime;
        if (viewingTime.getViewingTime() < 0) {
            viewTime = 0;
        }
        else if (viewingTime.getViewingTime() > this.content.getViewLength()) {
            viewTime = this.content.getViewLength();
        }
        else {
            viewTime = viewingTime.getViewingTime();
        }
        return viewTime;
    }
    
    /**
     * Composite Score for viewed Content
     * @return
     */
    public double getScore() {
        double compositeScore = this.normaliseViewingTime();
        if (this.normalisedRating > 0) {
            compositeScore *= this.normalisedRating;
        }
        else {
            compositeScore *= (double)RANGE / (double)2;
        }
        return compositeScore;
    }
    
    /**
     * Normalises the average viewing time to the content's total viewable length
     * @return
     */
    private double normaliseViewingTime() {
        return this.averageViewingTime / (double) this.content.getViewLength();
    }
    
    /**
     * Averages the viewing times and ratings of two content views
     * @param other
     */
    public void average(ContentView other) {
        if (this.normalisedRating > 0 && other.normalisedRating > 0) {
            int mergedRatingCount = this.ratingCount + other.ratingCount;
            double ratingTotalHere = this.normalisedRating * this.numberOfViews;
            double ratingTotalThere = other.normalisedRating * other.numberOfViews;
            double mergedRating = (ratingTotalHere + ratingTotalThere) / mergedRatingCount;
            this.normalisedRating = mergedRating;
            
        }
        int mergedNumberOfViews = this.numberOfViews + other.numberOfViews;
        double totalViewingTimeHere = this.averageViewingTime * this.numberOfViews;
        double totalViewingTimeThere = other.averageViewingTime * other.numberOfViews;
        double mergedViewingTime = totalViewingTimeHere + totalViewingTimeThere / mergedNumberOfViews;
        this.averageViewingTime = mergedViewingTime;
        this.numberOfViews = mergedNumberOfViews;
    }
}
