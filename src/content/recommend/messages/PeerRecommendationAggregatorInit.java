package content.recommend.messages;

import content.recommend.heuristic.AggregationHeuristic;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Initialises the PeerRecommendationAggregator with its heuristic
 * @author rory
 *
 */
public class PeerRecommendationAggregatorInit extends ActorMessage {
    private AggregationHeuristic heuristic;
    
    public PeerRecommendationAggregatorInit(AggregationHeuristic heuristic) {
        super(ActorMessageType.PeerRecommendationAggregatorInit);
        this.heuristic = heuristic;
    }
    
    public AggregationHeuristic getHeuristic() {
        return this.heuristic;
    }
}
