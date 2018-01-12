package tests.peer.communicate;

import akka.actor.ActorRef;
import akka.actor.Props;
import content.recommend.messages.PeerRecommendationRequest;
import peer.frame.core.ActorNames;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyRecommender extends DummyActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            DummyInit init = (DummyInit) message;
            super.logger = init.getLogger();
            this.createAggregator();
        }
        else if (message instanceof PeerRecommendationRequest) {
            PeerRecommendationRequest request = (PeerRecommendationRequest) message;
            super.logger.logMessage("Recommender received PeerRecommendationRequest");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("OriginalRequester: " + request.getOriginalRequester());
            super.logger.logMessage("OriginalTarget: " + request.getOriginalTarget());
            super.logger.logMessage("\n");
        }
    }
    
    private void createAggregator() {
        ActorRef aggregator = getContext().actorOf(Props.create(DummyAggregator.class), ActorNames.AGGREGATOR);
        PeerToPeerActorInit initPeerId = new PeerToPeerActorInit(super.peerId, ActorNames.AGGREGATOR);
        aggregator.tell(initPeerId, null);
        
        DummyInit dummyInit = new DummyInit(super.logger);
        aggregator.tell(dummyInit, null);
    }
}
