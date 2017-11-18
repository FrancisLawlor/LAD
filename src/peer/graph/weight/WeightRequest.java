package peer.graph.weight;

import core.ActorMessage;

/**
 * Actor Message that requests a Weighter's Weight
 * Requests weight of a theoretical link between the local user and a remote peer, 
 * which the Weighter (Weight Actor) represents
 *
 */
public class WeightRequest extends ActorMessage {
    private String peerId;
    
    public WeightRequest(String peerId) {
        this.peerId = peerId;
    }
    
    public String getPeerId() {
        return this.peerId;
    }
}
