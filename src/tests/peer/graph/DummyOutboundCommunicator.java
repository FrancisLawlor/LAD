package tests.peer.graph;

import peer.core.PeerToPeerActorInit;
import peer.graph.distributedmap.RemotePeerWeightedLinkAddition;
import peer.graph.weight.PeerWeightUpdateRequest;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyOutboundCommunicator extends DummyActor {
    
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
            super.logger.logMessage("PeerWeightUpdateRequest received in OutboundCommunicator");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("OriginalRequester: " + request.getOriginalRequester());
            super.logger.logMessage("OriginalTarget: " + request.getOriginalTarget());
            super.logger.logMessage("Weight: " + request.getNewWeight().getWeight());
            super.logger.logMessage("\n");
        }
        else if (message instanceof RemotePeerWeightedLinkAddition) {
            RemotePeerWeightedLinkAddition addition = (RemotePeerWeightedLinkAddition) message;
            super.logger.logMessage("RemotePeerWeightedLinkAddition received in OutboundCommunicator");
            super.logger.logMessage("Type: " + addition.getType().toString());
            super.logger.logMessage("OriginalRequester: " + addition.getOriginalRequester());
            super.logger.logMessage("OriginalTarget: " + addition.getOriginalTarget());
            super.logger.logMessage("Peer to add link to: " + addition.getLinkPeerId());
            super.logger.logMessage("Weight: " + addition.getLinkWeight().getWeight());
            super.logger.logMessage("\n");
        }
    }
}
