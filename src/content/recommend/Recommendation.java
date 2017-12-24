package content.recommend;

import content.core.Content;
import peer.core.UniversalId;

/**
 * Holds Recommended Content and originating Peer ID
 *
 */
public class Recommendation {
    private UniversalId peerId;
    private Content content;
    
    public Recommendation(UniversalId peerId, Content content) {
        this.peerId = peerId;
        this.content = content;
    }
    
    public UniversalId getRecommendingPeerId() {
        return this.peerId;
    }
    
    public String getContentId() {
        return this.content.getId();
    }
    
    public String getContentName() {
        return this.content.getFileName();
    }
    
    public String getContentType() {
        return this.content.getFileFormat();
    }
    
    public int getContentLength() {
        return this.content.getViewLength();
    }
    
    public Content getContent() {
        return this.content;
    }
}
