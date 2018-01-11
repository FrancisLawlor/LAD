package tests.peer.graph;

import peer.core.PeerToPeerActorInit;
import peer.data.BackupPeerLinkRequest;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyDatabaser extends DummyActor {
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
        else if (message instanceof BackupPeerLinkRequest) {
            BackupPeerLinkRequest request = (BackupPeerLinkRequest) message;
            super.logger.logMessage("Received BackupPeerLinkRequest in Databaser");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("Linked Peer Id: " + request.getPeerWeightedLink().getLinkedPeerId().toString());
            super.logger.logMessage("Linked Weight: " + request.getPeerWeightedLink().getLinkWeight().getWeight());
            super.logger.logMessage("");
        }
    }
}
