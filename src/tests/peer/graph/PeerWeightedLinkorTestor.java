package tests.peer.graph;

import akka.actor.ActorSelection;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.distributedmap.PeerWeightedLinkAddition;
import peer.graph.distributedmap.RemotePeerWeightedLinkAddition;
import peer.graph.link.PeerLinkExistenceRequest;
import peer.graph.link.PeerLinkExistenceResponse;
import peer.graph.link.PeerLinkResponse;
import peer.graph.link.PeerLinksRequest;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.PeerWeightUpdateRequest;
import peer.graph.weight.Weight;
import peer.graph.weight.WeightRequest;
import peer.graph.weight.WeightResponse;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class PeerWeightedLinkorTestor extends DummyActor {
    public static final String PEER_ONE = "PeerOne";
    private static final String PEER_TWO = "PeerTwo";
    private static final String PEER_THREE = "PeerThree";
    private static final String PEER_FOUR = "PeerFour";
    private static final String PEER_FIVE = "PeerFive";
    
    private UniversalId peerOneId = new UniversalId(PEER_ONE);
    private UniversalId peerTwoId = new UniversalId(PEER_TWO);
    private UniversalId peerThreeId = new UniversalId(PEER_THREE);
    private UniversalId peerFourId = new UniversalId(PEER_FOUR);
    private UniversalId peerFiveId = new UniversalId(PEER_FIVE);
    private int testNum = 0;
    
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
            switch (testNum) {
            case 0:
                this.startLinkAddition();
                break;
            case 1:
                this.startLinkExistenceTest();
                break;
            case 2:
                this.startLinkIterationTest();
                break;
            case 3:
                this.startRemoteWeightUpdateTest();
                break;
            case 4:
                this.startRemoteWeightUpdateCheckTest();
                break;
            case 5:
                this.startLocalWeightUpdateTest();
                break;
            case 6:
                this.startLocalWeightUpdateCheckTest();
                break;
            }
            testNum++;
        }
        else if (message instanceof PeerLinkExistenceResponse) {
            PeerLinkExistenceResponse response = (PeerLinkExistenceResponse) message;
            super.logger.logMessage("Received a PeerLinkExistenceResponse in the PeerWeightedLinkorTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("LinkChecked: " + response.getLinkToCheckPeerId());
            super.logger.logMessage("Link exists: " + response.isLinkInExistence());
            super.logger.logMessage("\n");
        }
        else if (message instanceof PeerLinkResponse) {
            PeerLinkResponse response = (PeerLinkResponse) message;
            super.logger.logMessage("Received a PeerLinkResponse in the PeerWeightedLinkorTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Link recorded between " + PEER_ONE + " and " + response.getPeerId());
            super.logger.logMessage("Checking weight of this link");
            super.logger.logMessage("\n");
            ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
            peerWeightedLinkor.tell(new WeightRequest(response.getPeerId()), getSelf());
        }
        else if (message instanceof WeightResponse) {
            WeightResponse response = (WeightResponse) message;
            super.logger.logMessage("Received a WeightResponse in the PeerWeightedLinkorTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Weighted Link between " + PEER_ONE + " and " + response.getLinkedPeerId());
            super.logger.logMessage("Weight of Link: " + response.getLinkWeight().getWeight());
            super.logger.logMessage("\n");
        }
    }
    
    protected void startLinkAddition() {
        PeerWeightedLinkAddition addPeerTwo = new PeerWeightedLinkAddition(peerTwoId, new Weight(2.0));
        PeerWeightedLinkAddition addPeerThree = new PeerWeightedLinkAddition(peerThreeId, new Weight(3.0));
        RemotePeerWeightedLinkAddition addPeerFour = new RemotePeerWeightedLinkAddition(peerFourId, peerOneId, new Weight(4.0));
        
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Adding a link between " + PEER_ONE + " and " + PEER_TWO + " to the Peer Graph");
        peerWeightedLinkor.tell(addPeerTwo, getSelf());
        super.logger.logMessage("Adding a link between " + PEER_ONE + " and " + PEER_THREE + " to the Peer Graph");
        peerWeightedLinkor.tell(addPeerThree, getSelf());
        super.logger.logMessage("Adding a link between " + PEER_FOUR + " and " + PEER_ONE + " to the Peer Graph");
        peerWeightedLinkor.tell(addPeerFour, getSelf());
        super.logger.logMessage("NOT adding a link between " + PEER_ONE + " and " + PEER_FIVE);
        super.logger.logMessage("\n");
    }
    
    protected void startLinkExistenceTest() {
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Checking link between " + PEER_ONE + " and " + PEER_TWO + " exists");
        peerWeightedLinkor.tell(new PeerLinkExistenceRequest(peerTwoId), getSelf());
        super.logger.logMessage("Checking link between " + PEER_ONE + " and " + PEER_THREE + " exists");
        peerWeightedLinkor.tell(new PeerLinkExistenceRequest(peerThreeId), getSelf());
        super.logger.logMessage("Checking link between " + PEER_ONE + " and " + PEER_FOUR + " exists");
        peerWeightedLinkor.tell(new PeerLinkExistenceRequest(peerFourId), getSelf());
        super.logger.logMessage("Checking link between " + PEER_ONE + " and " + PEER_FIVE + " exists");
        peerWeightedLinkor.tell(new PeerLinkExistenceRequest(peerFiveId), getSelf());
        super.logger.logMessage("\n");
    }
    
    protected void startLinkIterationTest() {
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Getting full list of Peer Links in the PeerGraph. They should return as individual messages of type PeerLinkResponse.");
        peerWeightedLinkor.tell(new PeerLinksRequest(), getSelf());
        super.logger.logMessage("\n");
    }
    
    protected void startRemoteWeightUpdateTest() {
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Sending PeerWeightUpdateRequest as if from " + PEER_TWO + " seeking to keep link weights consistent on both ends");
        peerWeightedLinkor.tell(new PeerWeightUpdateRequest(peerTwoId, super.peerId, new Weight(5.0)), getSelf());
        super.logger.logMessage("");
    }
    
    protected void startRemoteWeightUpdateCheckTest() {
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Checking Remote Peer Weight Update request from " + PEER_TWO + " has updated the weighted link between " + PEER_ONE + " and " + PEER_TWO + " in the Peer Graph of " + PEER_ONE + " to 7.0");
        peerWeightedLinkor.tell(new WeightRequest(peerTwoId), getSelf());
        super.logger.logMessage("");
    }
    
    protected void startLocalWeightUpdateTest() {
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Sending LocalWeightUpdateRequest which should also send PeerWeightUpdateRequest to " + PEER_TWO + " to keep weights consistent on both ends");
        peerWeightedLinkor.tell(new LocalWeightUpdateRequest(peerTwoId, new Weight(6.0)), getSelf());
        super.logger.logMessage("");
    }
    
    protected void startLocalWeightUpdateCheckTest() {
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        
        super.logger.logMessage("Checking Weight of link between " + PEER_ONE + " and " + PEER_TWO + " has been updated in the Peer Graph of " + PEER_ONE + " after Local Weight Update Request to 13.0");
        peerWeightedLinkor.tell(new WeightRequest(peerTwoId), getSelf());
        super.logger.logMessage("");
    }
}