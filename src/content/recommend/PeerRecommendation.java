package content.recommend;

import akka.actor.ActorRef;
import core.ActorMessage;

public class PeerRecommendation extends ActorMessage {
    private ActorRef originalRequester;
    
    public PeerRecommendation(ActorRef originalRequester) {
        this.originalRequester = originalRequester;
    }
    
    public ActorRef getOriginalRequester() {
        return this.originalRequester;
    }
}
