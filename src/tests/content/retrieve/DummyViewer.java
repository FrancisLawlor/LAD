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
            super.logger.logMessage("RetrievedContent received in Viewer" + "\n" +
                                    "Type: " + content.getType().toString() + "\n" +
                                    "Content ID: " + content.getContent().getId() + "\n" +
                                    "Content Name: " + content.getContent().getFileName() + "\n" +
                                    "Content Type: " + content.getContent().getFileFormat() + "\n" +
                                    "Content Length: " + content.getContent().getViewLength() + "\n" +
                                    "OriginalRequester: " + content.getOriginalRequester().toString() + "\n" +
                                    "OriginalTarget: " + content.getOriginalTarget() + "\n"
                                    );
        }
    }
}
