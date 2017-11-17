package graph.weight;

import core.ActorMessage;

/**
 * Actor Message that carries the Weighter's Weight
 * Sends back the weight of a theoretical link between the local user and a remote peer, 
 * which the Weighter (Weight Actor) represents
 *
 */
public class WeightResponse extends ActorMessage {
    private double linkWeight = 0.0;
    
    WeightResponse(double linkWeight) {
        this.linkWeight = linkWeight;
    }
    
    public double getLinkWeight() {
        return this.linkWeight;
    }
}
