package content.recommend.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerRequest;

/**
 * Requests a recommendation from the target peer for the requester peer
 *
 */
public class PeerRecommendationRequest extends PeerToPeerRequest {
    
    public PeerRecommendationRequest(UniversalId origin, UniversalId target) {
        super(ActorMessageType.PeerRecommendationRequest, origin, target);
    }
}
