package peer.data.messages;

import content.core.Content;
import peer.core.UniversalId;

import java.util.Set;

public class StoreSimilarPeerContentRequest {

    private Content content;
    private Set<UniversalId> similarPeers;

    public StoreSimilarPeerContentRequest(Content content, Set <UniversalId> similarPeers) {
        this.content = content;
        this.similarPeers = similarPeers;
    }

    public Content getContent() {
        return content;
    }

    public Set <UniversalId> getSimilarPeers() {
        return similarPeers;
    }

}
