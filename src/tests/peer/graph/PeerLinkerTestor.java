package tests.peer.graph;

import akka.actor.ActorSelection;
import core.ActorPaths;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.graph.link.PeerLinkAddition;
import peer.graph.link.PeerLinkExistenceRequest;
import peer.graph.link.PeerLinkExistenceResponse;
import peer.graph.link.PeerLinkResponse;
import peer.graph.link.PeerLinksRequest;
import peer.graph.weight.Weight;
import peer.graph.weight.WeightRequest;
import peer.graph.weight.WeightResponse;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class PeerLinkerTestor extends DummyActor {
    UniversalId peerTwoId = new UniversalId("PeerTwo");
    UniversalId peerThreeId = new UniversalId("PeerThree");
    UniversalId peerFourId = new UniversalId("PeerFour");
    UniversalId peerFiveId = new UniversalId("PeerFive");
    
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
        else if (message instanceof StartTest) {
            this.startPeerLinkerTest();
        }
        else if (message instanceof PeerLinkExistenceResponse) {
            PeerLinkExistenceResponse response = (PeerLinkExistenceResponse) message;
            super.logger.logMessage("Received a PeerLinkExistenceResponse in the PeerLinkerTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("LinkChecked: " + response.getLinkToCheckPeerId());
            super.logger.logMessage("Link exists: " + response.isLinkInExistence());
            super.logger.logMessage("\n");
        }
        else if (message instanceof PeerLinkResponse) {
            PeerLinkResponse response = (PeerLinkResponse) message;
            super.logger.logMessage("Received a PeerLinkResponse in the PeerLinkerTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Link recorded between our peer and peer: " + response.getPeerId());
            super.logger.logMessage("Checking weight of this link");
            super.logger.logMessage("\n");
            
            ActorSelection weighter = getContext().actorSelection(ActorPaths.getPathToWeighter(response.getPeerId()));
            weighter.tell(new WeightRequest(response.getPeerId()), getSelf());
        }
        else if (message instanceof WeightResponse) {
            WeightResponse response = (WeightResponse) message;
            super.logger.logMessage("Received a WeightResponse in the PeerLinkerTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Weighted Link with Peer: " + response.getLinkedPeerId());
            super.logger.logMessage("Weight of Link: " + response.getLinkWeight().getWeight());
            super.logger.logMessage("\n");
        }
    }
    
    protected void startPeerLinkerTest() {
        PeerLinkAddition addPeerTwo = new PeerLinkAddition(peerTwoId, new Weight(2.0));
        PeerLinkAddition addPeerThree = new PeerLinkAddition(peerThreeId, new Weight(3.0));
        PeerLinkAddition addPeerFour = new PeerLinkAddition(peerFourId, new Weight(4.0));
        
        ActorSelection peerLinker = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Adding a link to Peer Two to the Peer graph");
        peerLinker.tell(addPeerTwo, getSelf());
        super.logger.logMessage("Adding a link to Peer Three to the Peer graph");
        peerLinker.tell(addPeerThree, getSelf());
        super.logger.logMessage("Adding a link to Peer Four to the Peer graph");
        peerLinker.tell(addPeerFour, getSelf());
        super.logger.logMessage("\n");
        
        super.logger.logMessage("Checking link to Peer Two exists");
        peerLinker.tell(new PeerLinkExistenceRequest(peerTwoId), getSelf());
        super.logger.logMessage("Checking link to Peer Three exists");
        peerLinker.tell(new PeerLinkExistenceRequest(peerThreeId), getSelf());
        super.logger.logMessage("Checking link to Peer Four exists");
        peerLinker.tell(new PeerLinkExistenceRequest(peerFourId), getSelf());
        super.logger.logMessage("Checking link to Peer Five exists");
        peerLinker.tell(new PeerLinkExistenceRequest(peerFiveId), getSelf());
        super.logger.logMessage("\n");
        
        super.logger.logMessage("Getting full list of Peer Links");
        peerLinker.tell(new PeerLinksRequest(), getSelf());
        super.logger.logMessage("\n");
    }
}
