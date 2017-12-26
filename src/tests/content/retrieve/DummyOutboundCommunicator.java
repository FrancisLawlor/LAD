package tests.content.retrieve;

import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import peer.core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyOutboundCommunicator extends DummyActor {
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof PeerRetrieveContentRequest) {
            PeerRetrieveContentRequest request = (PeerRetrieveContentRequest) message;
            super.logger.logMessage("PeerRetrieveContentRequest received in OutboundCommunicator");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("Content ID: " + request.getContent().getId());
            super.logger.logMessage("Content Name: " + request.getContent().getFileName());
            super.logger.logMessage("Content Type: " + request.getContent().getFileFormat());
            super.logger.logMessage("Content Length: " + request.getContent().getViewLength());
            super.logger.logMessage("OriginalRequester: " + request.getOriginalRequester().toString());
            super.logger.logMessage("OriginalTarget: " + request.getOriginalTarget());
            super.logger.logMessage("");
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent content = (RetrievedContent) message;
            super.logger.logMessage("RetrievedContent received in OutboundCommunicator");
            super.logger.logMessage("Type: " + content.getType().toString());
            super.logger.logMessage("Content ID: " + content.getContent().getId());
            super.logger.logMessage("Content Name: " + content.getContent().getFileName());
            super.logger.logMessage("Content Type: " + content.getContent().getFileFormat());
            super.logger.logMessage("Content Length: " + content.getContent().getViewLength());
            super.logger.logMessage("OriginalRequester: " + content.getOriginalRequester().toString());
            super.logger.logMessage("OriginalTarget: " + content.getOriginalTarget());
            super.logger.logMessage("TransferHostIP: " + content.getTransferInfo().getTransferHostIp());
            super.logger.logMessage("TransferPort: " + content.getTransferInfo().getTransferPort());
            super.logger.logMessage("");
        }
    }
}
