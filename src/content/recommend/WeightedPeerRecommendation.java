package content.recommend;

public class WeightedPeerRecommendation {
    private PeerRecommendation peerRecommendation;
    private double weight;
    
    public WeightedPeerRecommendation(PeerRecommendation recommendation, double weight) {
        this.peerRecommendation = recommendation;
        this.weight = weight;
    }
    
    public PeerRecommendation getPeerRecommendation() {
        return this.peerRecommendation;
    }
    
    public double getWeight() {
        return this.weight;
    }
}
