package tests.content.retrieve;

import content.retrieve.RetrievedContent;
import peer.core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyViewer extends DummyActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent content = (RetrievedContent) message;
            super.logger.logMessage("RetrievedContent received in Viewer");
            super.logger.logMessage("Type: " + content.getType().toString());
            super.logger.logMessage("Content ID: " + content.getContent().getId());
            super.logger.logMessage("Content Name: " + content.getContent().getFileName());
            super.logger.logMessage("Content Type: " + content.getContent().getFileFormat());
            super.logger.logMessage("Content Length: " + content.getContent().getViewLength());
            super.logger.logMessage("OriginalRequester: " + content.getOriginalRequester().toString());
            super.logger.logMessage("OriginalTarget: " + content.getOriginalTarget());
            super.logger.logMessage("");
        }
    }
}
