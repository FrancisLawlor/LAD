package tests.content.recommend;

import akka.actor.ActorSelection;
import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActorInit;
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
        super.logger.logMessage("Received RecommendationsForUser in Viewer");
        super.logger.logMessage("Message type: " + recommendations.getType().toString());
        int i = 1;
        for (Recommendation recommendation : recommendations) {
            super.logger.logMessage("Recommendation no. " + i++ + ":");
            super.logger.logMessage("Content ID: " + recommendation.getContentId());
            super.logger.logMessage("Content Name: " + recommendation.getContentName());
            super.logger.logMessage("Content Type: " + recommendation.getContentType());
            super.logger.logMessage("Content Length: " + recommendation.getContentLength());
            super.logger.logMessage("");
        }
        super.logger.logMessage("\n");
    }
    
    private void getRecommendationsForUser() {
        ActorSelection recommender = getContext().actorSelection(ActorPaths.getPathToRecommender());
        recommender.tell(new RecommendationsForUserRequest(super.peerId), getSelf());
    }
}
