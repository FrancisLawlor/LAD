package tests.peer.communicate;

import content.impl.Content;
import content.recommend.PeerRecommendation;
import core.PeerToPeerActorInit;
import tests.actors.DummyActor;
import tests.actors.DummyInit;

public class DummyAggregator extends DummyActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            DummyInit init = (DummyInit) message;
            super.logger = init.getLogger();
        }
        else if (message instanceof PeerRecommendation) {
            PeerRecommendation recommendation = (PeerRecommendation) message;
            super.logger.logMessage("Aggregator received PeerRecommendation");
            super.logger.logMessage("Type: " + recommendation.getType().toString());
            super.logger.logMessage("OriginalRequester: " + recommendation.getOriginalRequester());
            super.logger.logMessage("OriginalTarget: " + recommendation.getOriginalTarget());
            for (Content content : recommendation) {
                super.logger.logMessage("Recommendation : " 
                    + "UID: " + content.getId() 
                    + " ; FileName: " + content.getFileName() 
                    + " ; ViewLength: "  + content.getViewLength());
            }
        }
    }
}
