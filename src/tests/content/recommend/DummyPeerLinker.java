package tests.content.recommend;

import akka.actor.ActorRef;
import akka.actor.Props;
import core.ActorNames;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.graph.link.PeerLinkResponse;
import peer.graph.link.PeerLinksRequest;
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
            super.logger = ((DummyInit)message).getLogger();
            this.initialiseWeights();
        }
        else if (message instanceof PeerLinksRequest) {
            PeerLinksRequest peerLinksRequest =
                    (PeerLinksRequest) message;
            this.processPeerLinksRequest(peerLinksRequest);
        }
    }
    
    protected void processPeerLinksRequest(PeerLinksRequest linksRequest) {
        super.logger.logMessage("PeerLinksRequest received");
        ActorRef sender = getSender();

        super.logger.logMessage("Sending \"Peer2\" as Linked Peer ID");
        UniversalId peerLinksId = new UniversalId("Peer2");
        PeerLinkResponse response = new PeerLinkResponse(peerLinksId);
        sender.tell(response, getSelf());

        super.logger.logMessage("Sending \"Peer3\" as Linked Peer ID");
        peerLinksId = new UniversalId("Peer3");
        response = new PeerLinkResponse(peerLinksId);
        sender.tell(response, getSelf());

        super.logger.logMessage("Sending \"Peer4\" as Linked Peer ID");
        peerLinksId = new UniversalId("Peer4");
        response = new PeerLinkResponse(peerLinksId);
        sender.tell(response, getSelf());
    }
    
    private void initialiseWeights() {
        UniversalId peerTwoId = new UniversalId("Peer2");
        UniversalId peerThreeId = new UniversalId("Peer3");
        UniversalId peerFourId = new UniversalId("Peer4");
        
        DummyInit dummyInit = new DummyInit(super.logger);
        
        final ActorRef dummyWeighterTwo = getContext().actorOf(Props.create(DummyWeighter.class), ActorNames.getWeighterName(peerTwoId));
        PeerToPeerActorInit init = new PeerToPeerActorInit(super.peerId, ActorNames.getWeighterName(peerTwoId));
        dummyWeighterTwo.tell(init, null);
        dummyWeighterTwo.tell(dummyInit, null);
        WeighterInit weighterInit = new WeighterInit(peerTwoId, new Weight(2));
        dummyWeighterTwo.tell(weighterInit, null);
        
        final ActorRef dummyWeighterThree = getContext().actorOf(Props.create(DummyWeighter.class), ActorNames.getWeighterName(peerThreeId));
        init = new PeerToPeerActorInit(super.peerId, ActorNames.getWeighterName(peerThreeId));
        dummyWeighterThree.tell(init, null);
        dummyWeighterThree.tell(dummyInit, null);
        weighterInit = new WeighterInit(peerThreeId, new Weight(3));
        dummyWeighterThree.tell(weighterInit, null);
        
        final ActorRef dummyWeighterFour = getContext().actorOf(Props.create(DummyWeighter.class), ActorNames.getWeighterName(peerFourId));
        init = new PeerToPeerActorInit(super.peerId, ActorNames.getWeighterName(peerFourId));
        dummyWeighterFour.tell(init, null);
        dummyWeighterFour.tell(dummyInit, null);
        weighterInit = new WeighterInit(peerFourId, new Weight(4));
        dummyWeighterFour.tell(weighterInit, null);
    }
}
