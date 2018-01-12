package content.recommend.core;

import content.recommend.messages.PeerRecommendation;
import peer.graph.core.Weight;

/**
 * Holds Recommendation from Peer
 * Holds Peer Weight to weigh Recommendation by
 *
 */
public class WeightedPeerRecommendation {
    private PeerRecommendation peerRecommendation;
    private Weight weight;
    
    public WeightedPeerRecommendation(PeerRecommendation recommendation, Weight weight) {
        this.peerRecommendation = recommendation;
        this.weight = weight;
    }
    
    public PeerRecommendation getPeerRecommendation() {
        return this.peerRecommendation;
    }
    
    public double getWeight() {
        return this.weight.getWeight();
    }
}
