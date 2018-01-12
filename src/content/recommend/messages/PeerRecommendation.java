package content.recommend.messages;

import java.util.Iterator;
import java.util.List;

import content.frame.core.Content;
import content.recommend.core.Recommendation;
import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerRequest;

/**
 * Curated List of Content a peer is recommending back to the requesting peer
 *
 */
public class PeerRecommendation extends PeerToPeerRequest implements Iterable<Recommendation> {
    private List<Content> contentList;
    
    public PeerRecommendation(List<Content> contentList, UniversalId origin, UniversalId target) {
        super(ActorMessageType.PeerRecommendation, origin, target);
        this.contentList = contentList;
    }
    
    public UniversalId getPeerId() {
        return super.getOriginalTarget();
    }
    
    public Recommendation getRecommendationAtRank(int rank) {
        return new Recommendation(super.getOriginalTarget(), contentList.get(rank));
    }
    
    public Iterator<Recommendation> iterator() {
        return new Iterator<Recommendation> () {
            private Iterator<Content> contentIter = contentList.iterator();
            
            public boolean hasNext() {
                return contentIter.hasNext();
            }
            
            public Recommendation next() {
                Content content = contentIter.next();
                return new Recommendation(getOriginalTarget(), content);
            }
        };
    }
    
    public int size() {
        return this.contentList.size();
    }
}
