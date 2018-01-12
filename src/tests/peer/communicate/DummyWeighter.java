package tests.peer.communicate;

import peer.frame.messages.PeerToPeerActorInit;
import peer.graph.messages.PeerWeightUpdateRequest;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyWeighter extends DummyActor {
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
        else if (message instanceof PeerWeightUpdateRequest) {
            PeerWeightUpdateRequest request = (PeerWeightUpdateRequest) message;
            super.logger.logMessage("Weighter received PeerWeightUpdateRequest");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("OriginalRequester: " + request.getOriginalRequester());
            super.logger.logMessage("OriginalTarget: " + request.getOriginalTarget());
            super.logger.logMessage("NewWeight: " + request.getNewWeight().getWeight());
            super.logger.logMessage("RequestingPeerId: " + request.getUpdateRequestingPeerId());
            super.logger.logMessage("TargetPeerId: "+ request.getTargetPeerId());
            super.logger.logMessage("\n");
        }
    }
}
