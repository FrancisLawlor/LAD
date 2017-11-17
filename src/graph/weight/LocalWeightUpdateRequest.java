package graph.weight;

import core.ActorMessage;

/**
 * Sent locally after content processing to change peer to peer link weight
 *
 */
public class LocalWeightUpdateRequest extends ActorMessage {
    private double newWeight = 0.0;
    
    LocalWeightUpdateRequest(double newWeight){
        this.newWeight = newWeight;
    }
    
    public double getNewWeight() {
        return this.newWeight;
    }
}
