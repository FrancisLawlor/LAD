package tests.peer.communicate;

import content.core.Content;
import content.recommend.PeerRecommendation;
import peer.core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

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
            super.logger.logMessage("\n");
        }
    }
}
