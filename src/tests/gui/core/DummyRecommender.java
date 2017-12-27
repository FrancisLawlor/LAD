package tests.gui.core;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import content.core.Content;
import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyRecommender extends DummyActor {
    private int requestCount = -1;
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof RecommendationsForUserRequest) {
            this.requestCount++;
            ActorRef sender = getSender();
            sender.tell(new RecommendationsForUser(getRecommendations()), getSelf());
        }
    }
    
    private List<Recommendation> getRecommendations() {
        List<Recommendation> recommendationList = new LinkedList<Recommendation>();
        for (int i = (requestCount * 10) + 1; i < ((requestCount + 1) * 10) + 1; i++) {
            recommendationList.add(new Recommendation(new UniversalId("localhost:10002"), new Content("ID_"+i, "FileName_"+i, "txt", i)));
        }
        return recommendationList;
    }
}
