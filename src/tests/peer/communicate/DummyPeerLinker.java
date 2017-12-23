package tests.peer.communicate;

import akka.actor.ActorRef;
import akka.actor.Props;
import core.ActorNames;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.graph.weight.Weight;
import peer.graph.weight.WeighterInit;
import tests.actors.DummyActor;
import tests.actors.DummyInit;

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
            this.createWeighters();
        }
    }
    
    private void createWeighters() {
        UniversalId peerOneId = new UniversalId(TestCommunicatorsSideSend.IP + ":" + TestCommunicatorsSideSend.PORT);
        
        ActorRef weighter = getContext().actorOf(Props.create(DummyWeighter.class), ActorNames.getWeighterName(peerOneId));
        PeerToPeerActorInit initPeerId = new PeerToPeerActorInit(super.peerId, ActorNames.getWeighterName(peerOneId));
        weighter.tell(initPeerId, null);
        
        WeighterInit weightInit = new WeighterInit(peerOneId, new Weight(10.0));
        weighter.tell(weightInit, null);
        
        DummyInit dummyInit = new DummyInit(super.logger);
        weighter.tell(dummyInit, null);
    }
}
