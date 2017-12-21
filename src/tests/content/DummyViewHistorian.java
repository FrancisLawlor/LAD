package tests.content;

import content.view.ViewHistoryRequest;
import core.PeerToPeerActorInit;

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
        
        
    }
}
