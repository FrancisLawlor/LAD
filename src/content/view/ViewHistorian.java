package content.view;

import java.util.List;

import akka.actor.ActorSelection;
import core.ActorPaths;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.xcept.UnknownMessageException;

/**
 * Keeps a record of the view history
 * Sends back the view history for peer recommendations
 *
 */
public class ViewHistorian extends PeerToPeerActor {
    private List<ContentView> viewHistory;
    
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

        ActorSelection generator = getContext().actorSelection(ActorPaths.getPathToGenerator());
        generator.tell(response, getSelf());
    }
}
