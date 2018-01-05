package peer.data.messages;

import content.core.Content;
import peer.core.UniversalId;

import java.util.Set;

public class StoredSimilarPeerContentResponse {
    private Content content;
    private Set<UniversalId> similarPeers;

    public StoredSimilarPeerContentResponse(Content content, Set <UniversalId> similarPeers) {
        this.content = content;
        this.similarPeers = similarPeers;
    }

    public Set <UniversalId> getSimilarPeers() {
        return similarPeers;
    }

    public Content getContent() {
        return content;
    }
}
