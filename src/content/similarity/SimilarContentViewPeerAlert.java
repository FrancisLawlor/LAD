package content.similarity;

import content.core.Content;
import content.view.ContentView;
import peer.core.ActorMessage;
import peer.core.ActorMessageType;
import peer.core.UniversalId;
import peer.graph.weight.Weight;

/**
 * Alerts Contenter that a peer has viewed similar content
 *
 */
public class SimilarContentViewPeerAlert extends ActorMessage {
    ContentView similarContentView;
    
    public SimilarContentViewPeerAlert(ContentView similarContentView) {
        super(ActorMessageType.PeerSimilarViewAlert);
        this.similarContentView = similarContentView;
    }
    
    public Content getSimilarViewContent() {
        return this.similarContentView.getContent();
    }
    
    public UniversalId getSimilarViewPeerId() {
        return this.similarContentView.getViewingPeerId();
    }
    
    public Weight getWeightToGive() {
        double compositeScore = this.similarContentView.getScore();
        return new Weight(compositeScore);
    }
}
