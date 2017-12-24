package tests.gui.core;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import content.core.Content;
import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import peer.core.UniversalId;

public class DummyRecommender extends UntypedActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof RecommendationsForUserRequest) {
            ActorRef sender = getSender();
            sender.tell(new RecommendationsForUser(getRecommendations()), getSelf());
        }
    }
    
    private List<Recommendation> getRecommendations() {
        List<Recommendation> recommendationList = new LinkedList<Recommendation>();
        for (int i = 1; i <= 10; i++) {
            recommendationList.add(new Recommendation(new UniversalId("localhost:10002"), new Content(""+i, ""+i, ""+i, ""+i, i)));
        }
        return recommendationList;
    }
}
