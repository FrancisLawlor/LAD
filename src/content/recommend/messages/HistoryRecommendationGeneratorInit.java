package content.recommend.messages;

import content.recommend.heuristic.HistoryHeuristic;
import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;

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
