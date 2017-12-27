package tests.content.retrieve;

import content.similarity.PeerSimilarViewAlert;
import peer.core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummySimilaritor extends DummyActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof PeerSimilarViewAlert) {
            PeerSimilarViewAlert alert = (PeerSimilarViewAlert) message;
            super.logger.logMessage("PeerSimilarViewAlert arrived in Similaritor");
            super.logger.logMessage("Type: " + alert.getType().toString());
            super.logger.logMessage("SimilarPeer: " + alert.getSimilarViewPeerId().toString());
            super.logger.logMessage("WeightToGive: " + alert.getWeightToGive().getWeight());
            super.logger.logMessage("");
        }
    }
}
