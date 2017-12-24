package content.recommend;

import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

/**
 * Requests a recommendation from the target peer for the requester peer
 *
 */
public class PeerRecommendationRequest extends PeerToPeerRequest {
    
    public PeerRecommendationRequest(UniversalId origin, UniversalId target) {
        super(ActorMessageType.PeerRecommendationRequest, origin, target);
    }
}
