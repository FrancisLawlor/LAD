package peer.data.messages;

import peer.core.UniversalId;

public class StorePeerLinkRequest {
    private UniversalId id;

    StorePeerLinkRequest(UniversalId id) {
        this.id = id;
    }

    public UniversalId getId() {
        return id;
    }
}

