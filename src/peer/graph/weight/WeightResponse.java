package peer.graph.weight;

import core.ActorMessage;
import core.UniversalId;

/**
 * Actor Message that carries the Weighter's Weight
 * Sends back the weight of a theoretical link between the local user and a remote peer, 
 * which the Weighter (Weight Actor) represents
 *
 */
public class WeightResponse extends ActorMessage {
    private UniversalId peerId;
    private Weight linkWeight;
    
    WeightResponse(UniversalId peerId, Weight linkWeight) {
        this.peerId = peerId;
        this.linkWeight = linkWeight;
    }
    
    public UniversalId getPeerId() {
        return this.peerId;
    }
    
    public Weight getLinkWeight() {
        return this.linkWeight;
    }
}
