package peer.graph.weight;

import core.ActorMessage;

/**
 * Actor Message that carries the Weighter's Weight
 * Sends back the weight of a theoretical link between the local user and a remote peer, 
 * which the Weighter (Weight Actor) represents
 *
 */
public class WeightResponse extends ActorMessage {
    private String peerId;
    private double linkWeight = 0.0;
    
    WeightResponse(String peerId, double linkWeight) {
        this.peerId = peerId;
        this.linkWeight = linkWeight;
    }
    
    public String getPeerId() {
        return this.peerId;
    }
    
    public double getLinkWeight() {
        return this.linkWeight;
    }
}
