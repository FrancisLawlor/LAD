package content.recommend;

import content.recommend.heuristic.HistoryHeuristic;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;

/**
 * Initialises the HistoryRecommendationGenerator with its heuristic
 * @author rory
 *
 */
public class HistoryRecommendationGeneratorInit extends ActorMessage {
    private UniversalId requestingPeerId;
    private HistoryHeuristic heuristic;
    
    public HistoryRecommendationGeneratorInit(UniversalId requestingPeerId, HistoryHeuristic heuristic) {
        super(ActorMessageType.HistoryRecommendationGeneratorInit);
        this.requestingPeerId = requestingPeerId;
        this.heuristic = heuristic;
    }
    
    public UniversalId getRequestingPeerId() {
        return this.requestingPeerId;
    }
    
    public HistoryHeuristic getHeuristic() {
        return this.heuristic;
    }
}
