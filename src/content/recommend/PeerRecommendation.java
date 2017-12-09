package content.recommend;

import java.util.Iterator;
import java.util.List;

import content.content.Content;
import core.ActorMessage;
import core.MessageTrace;

/**
 * Curated List of Content a peer is recommending back to the requesting peer
 *
 */
public class PeerRecommendation extends ActorMessage implements Iterable<Content> {
    private List<Content> contentList;
    
    public PeerRecommendation(List<Content> contentList, MessageTrace trace) {
        super(trace);
        this.contentList = contentList;
    }
    
    public Iterator<Content> iterator() {
        return contentList.iterator();
    }
    
    public Content getContentAtRank(int rank) {
        return contentList.get(rank);
    }
    
    public int size() {
        return this.contentList.size();
    }
}
