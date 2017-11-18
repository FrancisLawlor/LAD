package peer.graph.weight;

import core.ActorMessage;

/**
 * Sends Update request to the other peer in the theoretical peer to peer link
 * Weights need to be kept consistent on both sides of the theoretical link
 *
 */
public class PeerWeightUpdateRequest extends ActorMessage {
    private double newWeight = 0.0;
    
    PeerWeightUpdateRequest(double newWeight){
        this.newWeight = newWeight;
    }
    
    public double getNewWeight() {
        return this.newWeight;
    }
}
