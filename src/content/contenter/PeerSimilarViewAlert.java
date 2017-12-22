package content.contenter;

import core.ActorMessage;
import core.ActorMessageType;
import core.UniversalId;

/**
 * Alerts Contenter that a peer has viewed similar content
 *
 */
public class PeerSimilarViewAlert extends ActorMessage {
    private UniversalId similarViewPeerId;
    
    public PeerSimilarViewAlert() {
        super(ActorMessageType.PeerSimilarViewAlert);
    }
    
    public UniversalId getSimilarViewPeerId() {
        return this.similarViewPeerId;
    }
}
