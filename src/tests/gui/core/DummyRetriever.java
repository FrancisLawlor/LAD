package tests.gui.core;

import akka.actor.ActorRef;
import content.frame.core.Content;
import content.retrieve.messages.LocalRetrieveContentRequest;
import content.retrieve.messages.RetrievedContent;
import filemanagement.fileretrieval.MediaFileSaver;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyRetriever extends DummyActor {
    private static final String TEST = "A1B2C3D4E5F6G7H8I9J10";
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof LocalRetrieveContentRequest) {
            LocalRetrieveContentRequest request = (LocalRetrieveContentRequest) message;
            Content content = request.getContent();
            
            MediaFileSaver.writeMediaFile(content.getFileName(), content.getFileFormat(), TEST.getBytes());
            RetrievedContent retrievedContent = new RetrievedContent(request.getOriginalRequester(), request.getOriginalTarget(), content, null);
            
            ActorRef sender = getSender();
            sender.tell(retrievedContent, getSelf());
        }
    }
}
