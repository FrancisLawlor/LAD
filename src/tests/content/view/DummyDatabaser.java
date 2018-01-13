package tests.content.view;

import peer.data.messages.BackupContentViewInHistoryRequest;
import peer.frame.messages.PeerToPeerActorInit;
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
        else if (message instanceof BackupContentViewInHistoryRequest) {
            BackupContentViewInHistoryRequest request = (BackupContentViewInHistoryRequest) message;
            super.logger.logMessage("Received BackupContentViewInHistoryRequest in Databaser");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("Content: " + request.getContentView().getContent().getId());
            super.logger.logMessage("Content Score: " + request.getContentView().getScore());
            super.logger.logMessage("");
        }
    }
}
