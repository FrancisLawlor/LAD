package tests.content.view;

import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class ViewHistorianTestor extends DummyActor {
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
    }
}
