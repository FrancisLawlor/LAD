package content.view;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * Keeps a record of the view history
 * Sends back the view history for peer recommendations
 *
 */
public class ViewHistorian extends UntypedActor {
    private List<ContentView> viewHistory;
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof ViewHistoryRequest) {
            ViewHistoryRequest viewHistoryRequest = 
                    (ViewHistoryRequest) message;
            this.processViewHistoryRequest(viewHistoryRequest);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    /**
     * Sends back View History in a View History response
     * @param viewHistoryRequest
     */
    protected void processViewHistoryRequest(ViewHistoryRequest viewHistoryRequest) {
        ViewHistory history = new ViewHistory(this.viewHistory);
        ViewHistoryResponse response = new ViewHistoryResponse(history, viewHistoryRequest);

        ActorRef sender = getSender();
        sender.tell(response, sender);
    }
}
