package tests.peer.graph;

import core.PeerToPeerActorInit;
import peer.graph.link.PeerLinkExistenceResponse;
import peer.graph.link.PeerLinkResponse;
import peer.graph.weight.WeightResponse;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class PeerLinkerTestor extends DummyActor {
    
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
        else if (message instanceof StartTest) {
            this.startPeerLinkerTest();
        }
        else if (message instanceof PeerLinkExistenceResponse) {
            PeerLinkExistenceResponse response = (PeerLinkExistenceResponse) message;
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("LinkChecked: " + response.getLinkToCheckPeerId());
            super.logger.logMessage("Link exists: " + response.isLinkInExistence());
            super.logger.logMessage("\n");
        }
        else if (message instanceof PeerLinkResponse) {
            PeerLinkResponse response = (PeerLinkResponse) message;
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("This is one of the links recorded: " + response.getPeerId());
            super.logger.logMessage("\n");
        }
        else if (message instanceof WeightResponse) {
            WeightResponse response = (WeightResponse) message;
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Weighted Link with Peer: " + response.getPeerId());
            super.logger.logMessage("Weight of Link: " + response.getLinkWeight().getWeight());
            super.logger.logMessage("\n");
        }
    }
    
    protected void startPeerLinkerTest() {
        
    }
}
