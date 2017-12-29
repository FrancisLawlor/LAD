package tests.content.retrieve;

import akka.actor.ActorRef;
import content.core.ContentFile;
import content.core.ContentFileExistenceRequest;
import content.core.ContentFileExistenceResponse;
import content.core.ContentFileRequest;
import content.core.ContentFileResponse;
import content.retrieve.RetrievedContentFile;
import filemanagement.filewrapper.FileUnwrapper;
import peer.core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyDatabaser extends DummyActor {
    private int testCount = 0;

    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof ContentFileExistenceRequest) {
            ContentFileExistenceRequest request = (ContentFileExistenceRequest) message;
            super.logger.logMessage("ContentFileExistenceRequest received in Databaser");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("Content ID: " + request.getContent().getId());
            super.logger.logMessage("Content Name: " + request.getContent().getFileName());
            super.logger.logMessage("Content Type: " + request.getContent().getFileFormat());
            super.logger.logMessage("Content Length: " + request.getContent().getViewLength());
            super.logger.logMessage("");
            ContentFileExistenceResponse response;
            if (testCount == 0) {
                super.logger.logMessage("Databaser pretending the Content Does NOT exist in the Database");
                super.logger.logMessage("");
                response = new ContentFileExistenceResponse(request.getContent(), false);
                testCount++;
            }
            else {
                super.logger.logMessage("Databaser pretending the Content does exist in the Database");
                super.logger.logMessage("");
                response = new ContentFileExistenceResponse(request.getContent(), true);
            }
            ActorRef sender = getSender();
            sender.tell(response, getSelf());
        }
        else if (message instanceof ContentFileRequest) {
            ContentFileRequest request = (ContentFileRequest) message;
            super.logger.logMessage("ContentFileRequest received in Databaser");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("Content ID: " + request.getContent().getId());
            super.logger.logMessage("Content Name: " + request.getContent().getFileName());
            super.logger.logMessage("Content Type: " + request.getContent().getFileFormat());
            super.logger.logMessage("Content Length: " + request.getContent().getViewLength());
            super.logger.logMessage("");

            super.logger.logMessage("Sending Back a contentFile from the database with raw bytes of the string: " + TestHeaderMediaFile.TEST);
            super.logger.logMessage("");
            byte[] bytes = TestHeaderMediaFile.getHeaderMediaFile();
            ContentFile contentFile = new ContentFile(request.getContent(), bytes);
            ContentFileResponse response = new ContentFileResponse(contentFile);
            ActorRef sender = getSender();
            sender.tell(response, getSelf());
        }
        else if (message instanceof RetrievedContentFile) {
            RetrievedContentFile contentFile = (RetrievedContentFile) message;
            super.logger.logMessage("RetrievedContentFile received in Databaser" + "\n" +
                                    "Type: " + contentFile.getType().toString() + "\n" +
                                    "Content ID: " + contentFile.getContentFile().getContent().getId() + "\n" +
                                    "Content Name: " + contentFile.getContentFile().getContent().getFileName() + "\n" +
                                    "Content Type: " + contentFile.getContentFile().getContent().getFileFormat() + "\n" +
                                    "Content Length: " + contentFile.getContentFile().getContent().getViewLength() + "\n" +
                                    "Content ID: " + contentFile.getRetrievedContent().getContent().getId() + "\n" +
                                    "Content Name: " + contentFile.getRetrievedContent().getContent().getFileName() + "\n" +
                                    "Content Type: " + contentFile.getRetrievedContent().getContent().getFileFormat() + "\n" +
                                    "Content Length: " + contentFile.getRetrievedContent().getContent().getViewLength() + "\n" +
                                    "OriginalRequester: " + contentFile.getRetrievedContent().getOriginalRequester() + "\n" +
                                    "OriginalTarget: " + contentFile.getRetrievedContent().getOriginalTarget() + "\n" +
                                    "Test Bytes in ContentFile: " + TestHeaderMediaFile.getContentFileBytesAsString(contentFile.getContentFile().getBytes()) + "\n" +
                                    "Test Media String in ContentFile: " + new String(FileUnwrapper.extractFileArray(contentFile.getContentFile().getBytes())) + "\n"
                                    );
        }
    }
}
