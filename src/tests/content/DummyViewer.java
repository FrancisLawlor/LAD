package tests.content;

import akka.actor.ActorSelection;
import content.impl.Content;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import core.ActorPaths;
import core.PeerToPeerActorInit;

public class DummyViewer extends DummyActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
            this.getRecommendationsForUser();
        }
        else if (message instanceof RecommendationsForUser) {
            RecommendationsForUser recommendations = (RecommendationsForUser) message;
            this.processRecommendationsForUser(recommendations);
        }
    }
    
    protected void processRecommendationsForUser(RecommendationsForUser recommendations) {
        StringBuffer buffer = new StringBuffer();
        int i = 1;
        for (Content content : recommendations) {
            buffer.append("Recommendation " + i++ + ": " + content.getFileName() + "\n");
        }
        super.logger.logMessage(buffer.toString());
    }
    
    private void getRecommendationsForUser() {
        ActorSelection recommender = getContext().actorSelection(ActorPaths.getPathToRecommender());
        recommender.tell(new RecommendationsForUserRequest(super.peerId), getSelf());
    }
}
