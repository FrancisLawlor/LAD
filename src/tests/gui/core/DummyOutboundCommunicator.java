package tests.gui.core;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import akka.actor.ActorRef;
import content.core.Content;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import content.retrieve.TransferInfo;
import content.view.ContentView;
import content.view.ContentViews;
import filemanagement.filewrapper.ArrayToLongConverter;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
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
            requestCount++;
            
            PeerRecommendation recommendation = new PeerRecommendation(contentList, request.getOriginalRequester(), request.getOriginalTarget());
            
            ActorRef sender = getSender();
            sender.tell(recommendation, getSelf());
        }
        else if (message instanceof PeerRetrieveContentRequest) {
            PeerRetrieveContentRequest request = (PeerRetrieveContentRequest) message;
            Content content = request.getContent();
            TransferInfo transferInfo = new TransferInfo(new UniversalId("localhost:10003"), "10003");
            RetrievedContent retrievedContent = new RetrievedContent(request.getOriginalRequester(), request.getOriginalTarget(), content, transferInfo);
            ActorRef sender = getSender();
            sender.tell(retrievedContent, getSelf());
            transfer();
        }
    }
    
    private void transfer() throws Exception {
        ServerSocket serverSocket = new ServerSocket(10003);
        Socket sendSocket = serverSocket.accept();
        DataOutputStream stream = new DataOutputStream(sendSocket.getOutputStream());
        stream.write(getHeaderMediaFile());
        sendSocket.close();
        serverSocket.close(); 
    }
    
    private static byte[] getHeaderMediaFile() {
        Content content = new Content("UniqueId", "Filename", "FileFormat", 10);
        ContentViews contentViews = new ContentViews();
        contentViews.addContentView(new ContentView(content, new UniversalId("localhost:10010")));
        contentViews.addContentView(new ContentView(content, new UniversalId("localhost:10011")));
        contentViews.addContentView(new ContentView(content, new UniversalId("localhost:10012")));
        contentViews.addContentView(new ContentView(content, new UniversalId("localhost:10013")));
        contentViews.addContentView(new ContentView(content, new UniversalId("localhost:10014")));
        contentViews.addContentView(new ContentView(content, new UniversalId("localhost:10015")));
        Gson gson = new Gson();
        String json = gson.toJson(contentViews);
        
        byte[] headerArray = json.getBytes();
        long headerLength = headerArray.length;
        
        byte[] mediaArray = TEST.getBytes();
        
        int fileLength = Long.BYTES + headerArray.length + mediaArray.length;
        byte[] file = new byte[fileLength];
        
        byte[] headerLengthAsByteArray = ArrayToLongConverter.longToByteArray(headerLength);
        
        int i;
        
        for (i = 0; i < Long.BYTES; i++) {
            file[i] = headerLengthAsByteArray[i];
        }
        
        for (int j = 0; j < headerArray.length; j++) {
            file[i] = headerArray[j];
            i++;
        }
        
        for (int j = 0; j < mediaArray.length; j++) {
            file[i] = mediaArray[j];
            i++;
        }
        return file;
    }
}
