package peer.graph.core;

/**
 * Weight of theoretical link between two peers
 *
 */
public class Weight {
    private double weight;
    
    public Weight() {
        this.weight = 0.0;
    }
    
    public Weight(double weight) {
        this.weight = weight;
    }
    
    public void add(Weight other) {
        this.weight += other.weight;
    }
    
    public double getWeight() {
        return this.weight;
    }
}
