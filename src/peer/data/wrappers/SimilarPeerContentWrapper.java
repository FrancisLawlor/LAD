package peer.data.wrappers;

import content.core.Content;
import peer.core.UniversalId;

import java.util.Set;

public class SimilarPeerContentWrapper {
    private Content content;
    private Set<UniversalId> similarPeerIds;

    public SimilarPeerContentWrapper(Content content, Set <UniversalId> similarPeerIds) {
        this.content = content;
        this.similarPeerIds = similarPeerIds;
    }

    public Content getContent() {
        return content;
    }

    public Set <UniversalId> getSimilarPeerIds() {
        return similarPeerIds;
    }

}
