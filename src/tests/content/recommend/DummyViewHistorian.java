package tests.content.recommend;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import content.core.Content;
import content.view.ContentView;
import content.view.ViewHistory;
import content.view.ViewHistoryRequest;
import content.view.ViewHistoryResponse;
import content.view.ViewingTime;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyViewHistorian extends DummyActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof ViewHistoryRequest) {
            ViewHistoryRequest viewHistoryRequest = 
                    (ViewHistoryRequest) message;
            this.processViewHistoryRequest(viewHistoryRequest);
        }
    }
    
    protected void processViewHistoryRequest(ViewHistoryRequest viewHistoryRequest) {
        super.logger.logMessage("ViewHistoryRequest received");
        
        ViewHistory viewHistory = getViewHistory(getContentViews(getContent()));
        ViewHistoryResponse response = new ViewHistoryResponse(viewHistory, viewHistoryRequest);
        
        super.logger.logMessage("Sending fake viewHistory");
        ActorRef generator = getSender();
        generator.tell(response, getSelf());
    }
    
    private List<Content> getContent() {
        List<Content> contentList = new LinkedList<Content>();
        for (int i = 1; i <= 20; i++) {
            contentList.add(new Content(""+i, ""+i, ""+i, 20));
            super.logger.logMessage("Creating fake Content with id and name: " + i + " and view length 20");
        }
        return contentList;
    }
    
    private List<ContentView> getContentViews(List<Content> contentList) {
        List<ContentView> contentViews = new LinkedList<ContentView>();
        for (int i = 1; i <= contentList.size(); i++) {
            ContentView contentView = new ContentView(contentList.get(i - 1), new UniversalId("Peer" + (i + 10)));
            if (i % 2 == 0) {
                super.logger.logMessage("Recording full view for content " + i);
                contentView.recordView(new ViewingTime(20));
            }
            else {
                super.logger.logMessage("Recording zero view for content " + i);
                contentView.recordView(new ViewingTime(0));
                
            }
            contentViews.add(contentView);
        }
        return contentViews;
    }
    
    private ViewHistory getViewHistory(List<ContentView> contentViews) {
        ViewHistory viewHistory = new ViewHistory(contentViews);
        return viewHistory;
    }
}
