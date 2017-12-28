package content.view;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.xcept.UnknownMessageException;

/**
 * Keeps a record of the view history
 * Sends back the view history for peer recommendations
 *
 */
public class ViewHistorian extends PeerToPeerActor {
    private List<ContentView> viewHistory;
    
    public ViewHistorian() {
        this.viewHistory = new LinkedList<ContentView>();
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
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Sends back View History in a View History response
     * @param viewHistoryRequest
     */
    protected void processViewHistoryRequest(ViewHistoryRequest viewHistoryRequest) {
        ViewHistory history = new ViewHistory(this.viewHistory);
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
        ContentView contentView = recordContentView.getContentView();
        this.viewHistory.add(contentView);
        
        ContentViewAddition addition = new ContentViewAddition(contentView);
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        databaser.tell(addition, getSelf());
    }
}
