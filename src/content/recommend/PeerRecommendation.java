package content.recommend;

import java.util.Iterator;
import java.util.List;

import content.Content;
import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

/**
 * Curated List of Content a peer is recommending back to the requesting peer
 *
 */
public class PeerRecommendation extends RequestCommunication implements Iterable<Content> {
    private List<Content> contentList;
    
    public PeerRecommendation(List<Content> contentList, UniversalId origin, UniversalId target) {
        super(ActorMessageType.PeerRecommendation, origin, target);
        this.contentList = contentList;
    }
    
    public UniversalId getPeerId() {
        return super.getOriginalTarget();
    }
    
    public Content getContentAtRank(int rank) {
        return contentList.get(rank);
    }
    
    public Iterator<Content> iterator() {
        return contentList.iterator();
    }
    
    public int size() {
        return this.contentList.size();
    }
}
