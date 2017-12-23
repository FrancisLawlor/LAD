package tests.peer.communicate;

import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyRetriever extends DummyActor {
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
        else if (message instanceof RetrievedContent) {
            RetrievedContent content = (RetrievedContent) message;
            super.logger.logMessage("Retriever received RetrievedContent");
            super.logger.logMessage("Type: " + content.getType().toString());
            super.logger.logMessage("OriginalRequester: " + content.getOriginalRequester());
            super.logger.logMessage("OriginalTarget: " + content.getOriginalTarget());
            super.logger.logMessage("UID: " + content.getContent().getId()
                    + " ; Filename: " + content.getContent().getFileName()
                    + " ; FileFormat: " + content.getContent().getFileFormat()
                    + " ; ViewLength: " + content.getContent().getViewLength()
                    );
        }
        else if (message instanceof PeerRetrieveContentRequest) {
            PeerRetrieveContentRequest request = (PeerRetrieveContentRequest) message;
            super.logger.logMessage("Retriever received RetrieveContentRequest"); 
            super.logger.logMessage("Type: "+ request.getType().toString());
            super.logger.logMessage("OriginalRequester: " + request.getOriginalRequester()); 
            super.logger.logMessage("OriginalTarget: " + request.getOriginalTarget());
        }
    }
}
