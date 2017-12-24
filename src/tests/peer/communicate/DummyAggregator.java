package tests.peer.communicate;

import content.recommend.PeerRecommendation;
import content.recommend.Recommendation;
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
            PeerRecommendation peerRecommendation = (PeerRecommendation) message;
            super.logger.logMessage("Aggregator received PeerRecommendation");
            super.logger.logMessage("Type: " + peerRecommendation.getType().toString());
            super.logger.logMessage("OriginalRequester: " + peerRecommendation.getOriginalRequester());
            super.logger.logMessage("OriginalTarget: " + peerRecommendation.getOriginalTarget());
            for (Recommendation recommendation : peerRecommendation) {
                super.logger.logMessage("Recommendation : " 
                    + "UID: " + recommendation.getContentId() 
                    + " ; FileName: " + recommendation.getContentName() 
                    + " ; ViewLength: "  + recommendation.getContentLength());
            }
            super.logger.logMessage("\n");
        }
    }
}
