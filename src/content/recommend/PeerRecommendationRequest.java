package content.recommend;

import akka.actor.ActorRef;

public class PeerRecommendationRequest {
    private ActorRef requester;
    
    PeerRecommendationRequest(ActorRef requester) {
        this.requester = requester;
    }
    
    public ActorRef getRequester() {
        return this.requester;
    }
}
