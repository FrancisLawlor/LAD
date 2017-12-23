package tests.gui.core;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import content.impl.Content;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;

public class DummyRecommender extends UntypedActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof RecommendationsForUserRequest) {
            ActorRef sender = getSender();
            sender.tell(new RecommendationsForUser(getContent()), getSelf());
        }
    }
    
    private List<Content> getContent() {
        List<Content> contentList = new LinkedList<Content>();
        for (int i = 1; i <= 10; i++) {
            contentList.add(new Content(""+i, ""+i, ""+i, ""+i, i));
        }
        return contentList;
    }
}
