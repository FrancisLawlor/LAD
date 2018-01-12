package content.similarity.messages;

import content.frame.core.Content;
import content.view.core.ContentView;
import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.ActorMessage;
import peer.graph.core.Weight;

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
