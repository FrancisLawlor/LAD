package content.contenter;

import core.PeerToPeerActor;

/**
 * Updates theoretical graph based on Header data from Retrieved Content
 * Updates PeerLinker and Weighters to increase weight of links between peers that viewed similar content to this peer
 *
 */
public class Contenter extends PeerToPeerActor {
    
    @Override
    public void onReceive(Object message) {
        
    }
}
