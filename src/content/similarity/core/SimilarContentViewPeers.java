package content.similarity.core;

import content.frame.core.Content;

/**
 * Encapsulates peers who've watched the same content
 *
 */
public class SimilarContentViewPeers {
    private Content content;
    private SimilarViewPeers similarViewPeers;
    
    public SimilarContentViewPeers(Content content, SimilarViewPeers similarViewPeers) {
        this.content = content;
        this.similarViewPeers = similarViewPeers;
    }
    
    public Content getContent() {
        return this.content;
    }
    
    public SimilarViewPeers getSimilarViewPeers() {
        return this.similarViewPeers;
    }
}
