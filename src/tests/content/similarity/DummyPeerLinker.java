package tests.content.similarity;

import peer.core.PeerToPeerActorInit;
import peer.graph.link.PeerLinkAddition;
import peer.graph.link.PeerLinkExistenceRequest;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyPeerLinker extends DummyActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            DummyInit init = (DummyInit) message;
            super.logger = init.getLogger();
        }
        else if (message instanceof PeerLinkExistenceRequest) {
            
        }
        else if (message instanceof PeerLinkAddition) {
            
        }
    }
}
