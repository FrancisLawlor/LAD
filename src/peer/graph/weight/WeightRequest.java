package peer.graph.weight;

import core.ActorMessage;
import core.UniversalId;

/**
 * Actor Message that requests a Weighter's Weight
 * Requests weight of a theoretical link between the local user and a remote peer, 
 * which the Weighter (Weight Actor) represents
 *
 */
public class WeightRequest extends ActorMessage {
    private UniversalId peerId;
    
    public WeightRequest(UniversalId peerId) {
        this.peerId = peerId;
    }
    
    public UniversalId getPeerId() {
        return this.peerId;
    }
}
