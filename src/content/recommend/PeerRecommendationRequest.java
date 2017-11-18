package content.recommend;

import akka.actor.ActorRef;

/**
 * Requests a recommendation from this peer for the requester peer
 *
 */
public class PeerRecommendationRequest {
    private ActorRef requester;
    
    PeerRecommendationRequest(ActorRef requester) {
        this.requester = requester;
    }
    
    public ActorRef getRequester() {
        return this.requester;
    }
}
