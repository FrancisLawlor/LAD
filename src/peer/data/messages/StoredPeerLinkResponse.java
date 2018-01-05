package peer.data.messages;

import peer.core.UniversalId;

public class StoredPeerLinkResponse {
    private UniversalId universalId;

    public StoredPeerLinkResponse(UniversalId universalId) {
        this.universalId = universalId;
    }

    public UniversalId getUniversalId() {
        return universalId;
    }

}
