package tests.content.recommend;

import akka.actor.ActorSelection;
import content.impl.Content;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import core.ActorPaths;
import core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class DummyViewer extends DummyActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof RecommendationsForUser) {
            RecommendationsForUser recommendations = (RecommendationsForUser) message;
            this.processRecommendationsForUser(recommendations);
        }
        else if (message instanceof StartTest) {
            this.getRecommendationsForUser();
        }
    }
    
    protected void processRecommendationsForUser(RecommendationsForUser recommendations) {
        int i = 1;
        for (Content content : recommendations) {
            super.logger.logMessage("Recommendation " + i++ + ": " + content.getFileName());
        }
    }
    
    private void getRecommendationsForUser() {
        ActorSelection recommender = getContext().actorSelection(ActorPaths.getPathToRecommender());
        recommender.tell(new RecommendationsForUserRequest(super.peerId), getSelf());
    }
}
