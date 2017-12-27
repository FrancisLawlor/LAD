package tests.gui.core;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import content.core.Content;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import filemanagement.fileretrieval.FileManager;
import peer.core.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyOutboundCommunicator extends DummyActor {
    private static final String TEST = "A1B2C3D4E5F6G7H8I9J10";
    
    private int requestCount = 0;
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof PeerRecommendationRequest) {
            PeerRecommendationRequest request = (PeerRecommendationRequest) message;
            
            List<Content> contentList = new LinkedList<Content>();
            for (int i = (requestCount * 10) + 1; i < ((requestCount + 1) * 10) + 1; i++) {
                contentList.add(new Content("ID_"+i, "FileName_"+i, "txt", i));
            }
            
            PeerRecommendation recommendation = new PeerRecommendation(contentList, request.getOriginalRequester(), request.getOriginalTarget());
            
            ActorRef sender = getSender();
            sender.tell(recommendation, getSelf());
        }
        else if (message instanceof PeerRetrieveContentRequest) {
            PeerRetrieveContentRequest request = (PeerRetrieveContentRequest) message;
            Content content = request.getContent();
            
            FileManager.writeMediaFile(content.getFileName(), content.getFileFormat(), TEST.getBytes());
            RetrievedContent retrievedContent = new RetrievedContent(request.getOriginalRequester(), request.getOriginalTarget(), content, null);
            
            ActorRef sender = getSender();
            sender.tell(retrievedContent, getSelf());
        }
    }
}
