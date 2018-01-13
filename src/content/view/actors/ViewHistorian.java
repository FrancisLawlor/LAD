package content.view.actors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import content.frame.core.Content;
import content.view.core.ContentView;
import content.view.core.ViewHistory;
import content.view.messages.ContentViewAddition;
import content.view.messages.RecordContentView;
import content.view.messages.ViewHistoryRequest;
import content.view.messages.ViewHistoryResponse;
import peer.data.messages.BackedUpContentViewResponse;
import peer.data.messages.BackupContentViewInHistoryRequest;
import peer.frame.actors.PeerToPeerActor;
import peer.frame.core.ActorPaths;
import peer.frame.exceptions.UnknownMessageException;
import peer.frame.messages.PeerToPeerActorInit;

/**
 * Keeps a record of the view history
 * Sends back the view history for peer recommendations
 *
 */
public class ViewHistorian extends PeerToPeerActor {
    private Map<Content, ContentView> viewHistory;
    
    public ViewHistorian() {
        this.viewHistory = new HashMap<Content, ContentView>();
    }
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof ViewHistoryRequest) {
            ViewHistoryRequest viewHistoryRequest = 
                    (ViewHistoryRequest) message;
            this.processViewHistoryRequest(viewHistoryRequest);
        }
        else if (message instanceof RecordContentView) {
            RecordContentView recordContentView = (RecordContentView) message;
            this.processRecordContentView(recordContentView);
        }
        else if (message instanceof BackedUpContentViewResponse) {
            BackedUpContentViewResponse response = (BackedUpContentViewResponse) message;
            this.processBackedUpContentViewResponse(response);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Sends back View History in a View History response
     * @param viewHistoryRequest
     */
    protected void processViewHistoryRequest(ViewHistoryRequest viewHistoryRequest) {
        List<ContentView> contentViewsFromHistory = new LinkedList<ContentView>();
        Iterator<ContentView> historyIterator = this.viewHistory.values().iterator();
        while (historyIterator.hasNext()) {
            contentViewsFromHistory.add(historyIterator.next());
        }
        ViewHistory history = new ViewHistory(contentViewsFromHistory);
        ViewHistoryResponse response = new ViewHistoryResponse(history, viewHistoryRequest);
        
        ActorRef generator = getSender();
        generator.tell(response, getSelf());
    }
    
    /**
     * Records a Content View in the View History
     * Tells the Databaser to add this peer's Content View to the Content Views header on the stored ContentFile
     * @param recordContentView
     */
    protected void processRecordContentView(RecordContentView recordContentView) {
        ContentView newContentView = recordContentView.getContentView();
        Content content = newContentView.getContent();
        if (this.viewHistory.containsKey(content)) {
            ContentView storedContentView = this.viewHistory.get(content);
            storedContentView.average(newContentView);
            BackupContentViewInHistoryRequest backUpRequest = new BackupContentViewInHistoryRequest(storedContentView);
            ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
            databaser.tell(backUpRequest, getSelf());
        }
        else {
            this.viewHistory.put(content, newContentView);
        }
        ContentViewAddition addition = new ContentViewAddition(newContentView);
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        databaser.tell(addition, getSelf());
    }
    
    /**
     * Re-adds a backed up Content View to the View History from backup at startup
     * @param response
     */
    protected void processBackedUpContentViewResponse(BackedUpContentViewResponse response) {
        ContentView backedUpContentView = response.getContentView();
        this.viewHistory.put(backedUpContentView.getContent(), backedUpContentView);
    }
}
