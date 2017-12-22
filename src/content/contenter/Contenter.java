package content.contenter;

import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import peer.graph.link.PeerLinkExistenceResponse;

/**
 * Updates theoretical graph based on Header data from Retrieved Content
 * Updates PeerLinker and Weighters to increase weight of links between peers that viewed similar content to this peer
 *
 */
public class Contenter extends PeerToPeerActor {
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof PeerSimilarViewAlert) {
            PeerSimilarViewAlert alert = (PeerSimilarViewAlert) message;
            this.processPeerSimilarViewAlert(alert);
        }
        else if (message instanceof PeerLinkExistenceResponse) {
            PeerLinkExistenceResponse response = (PeerLinkExistenceResponse) message;
            this.processPeerLinkExistenceResponse(response);
        }
    }
    
    protected void processPeerSimilarViewAlert(PeerSimilarViewAlert notify) {
        
    }
    
    protected void processPeerLinkExistenceResponse(PeerLinkExistenceResponse response) {
        
    }
}
