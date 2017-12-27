package content.similarity;

import content.view.ContentView;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;
import peer.graph.weight.Weight;

/**
 * Alerts Contenter that a peer has viewed similar content
 *
 */
public class PeerSimilarViewAlert extends ActorMessage {
    ContentView similarContentView;
    
    public PeerSimilarViewAlert(ContentView similarContentView) {
        super(ActorMessageType.PeerSimilarViewAlert);
        this.similarContentView = similarContentView;
    }
    
    public UniversalId getSimilarViewPeerId() {
        return this.similarContentView.getViewingPeerId();
    }
    
    public Weight getWeightToGive() {
        double compositeScore = this.similarContentView.getScore();
        return new Weight(compositeScore);
    }
}
