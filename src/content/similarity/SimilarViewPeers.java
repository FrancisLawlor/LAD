package content.similarity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import peer.core.UniversalId;

/**
 * Set of Peers who watched this same content and are thus similar to this peer and each other
 *
 */
public class SimilarViewPeers implements Iterable<UniversalId> {
    private Set<UniversalId> similarContentViewPeers;
    
    public SimilarViewPeers() {
        this.similarContentViewPeers = new HashSet<UniversalId>();
    }
    
    public void add(UniversalId similarContentViewPeerId) {
        this.similarContentViewPeers.add(similarContentViewPeerId);
    }
    
    public Iterator<UniversalId> iterator() {
        return this.similarContentViewPeers.iterator();
    }
}
