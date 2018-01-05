package peer.data.messages;

import peer.core.UniversalId;
import peer.graph.weight.Weight;

public class StoredPeerLinkWeightResponse {
    private UniversalId universalId;
    private Weight linkedWeight;

    public StoredPeerLinkWeightResponse(UniversalId universalId, Weight linkedWeight) {
        this.universalId = universalId;
        this.linkedWeight = linkedWeight;
    }

    public UniversalId getUniversalId() {
        return universalId;
    }

    public Weight getLinkedWeight() {
        return linkedWeight;
    }
}
