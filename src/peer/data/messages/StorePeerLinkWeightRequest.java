package peer.data.messages;

import peer.core.UniversalId;
import peer.graph.weight.Weight;

public class StorePeerLinkWeightRequest {
    private UniversalId id;
    private Weight weight;


    public StorePeerLinkWeightRequest(UniversalId id, Weight weight) {
        this.id = id;
        this.weight = weight;
    }

    public Weight getWeight() {
        return weight;
    }

    public UniversalId getId() {
        return id;
    }

}
