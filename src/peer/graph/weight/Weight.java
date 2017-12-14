package peer.graph.weight;

/**
 * Weight of theoretical link between two peers
 *
 */
public class Weight {
    private double weight;
    
    public Weight(double weight) {
        this.weight = weight;
    }
    
    public double getWeight() {
        return this.weight;
    }
}
