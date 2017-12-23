package tests.peer.graph;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import core.ActorNames;
import core.ActorPaths;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.graph.weight.LocalWeightUpdateRequest;
import peer.graph.weight.PeerWeightUpdateRequest;
import peer.graph.weight.Weight;
import peer.graph.weight.WeightRequest;
import peer.graph.weight.WeightResponse;
import peer.graph.weight.Weighter;
import peer.graph.weight.WeighterInit;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class WeighterTestor extends DummyActor {
    private UniversalId peerTwoId;
    private int testNum = 1;
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            DummyInit init = (DummyInit) message;
            super.logger = init.getLogger();
        }
        else if (message instanceof StartTest) {
            this.setupWeighterTest();
            this.startWeighterTest1();
            this.testNum++;
        }
        else if (message instanceof WeightResponse) {
            WeightResponse response = (WeightResponse) message;
            super.logger.logMessage("WeightResponse received back in WeighterTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Represents link with: " + response.getLinkedPeerId());
            super.logger.logMessage("LinkWeight: " + response.getLinkWeight().getWeight());
            super.logger.logMessage("\n");
            
            switch (testNum) {
            case 2:
                this.startWeighterTest2();
                testNum++;
                break;
            case 3:
                this.startWeighterTest3();
                testNum++;
                break;
            }
        }
    }
    
    protected void setupWeighterTest() {
        peerTwoId = new UniversalId("PeerTwo");
        
        final ActorRef weighter = getContext().actorOf(Props.create(Weighter.class), ActorNames.getWeighterName(peerTwoId));
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.getWeighterName(peerTwoId));
        weighter.tell(peerIdInit, getSelf());
        WeighterInit weighterInit = new WeighterInit(peerTwoId, new Weight(10.0));
        weighter.tell(weighterInit, getSelf());
    }
    
    protected void startWeighterTest1() {
        ActorSelection weighter = getContext().actorSelection(ActorPaths.getPathToWeighter(peerTwoId));
        super.logger.logMessage("Sending WeightRequest");
        weighter.tell(new WeightRequest(peerTwoId), getSelf());
    }
    
    protected void startWeighterTest2() {
        ActorSelection weighter = getContext().actorSelection(ActorPaths.getPathToWeighter(peerTwoId));
        super.logger.logMessage("Sending PeerWeightUpdateRequest as if from another peer keeping link weights consistent on both ends");
        weighter.tell(new PeerWeightUpdateRequest(peerTwoId, super.peerId, new Weight(5.0)), getSelf());
        super.logger.logMessage("Checking Weight has been updated");
        weighter.tell(new WeightRequest(peerTwoId), getSelf());
    }
    
    protected void startWeighterTest3() throws Throwable {
        ActorSelection weighter = getContext().actorSelection(ActorPaths.getPathToWeighter(peerTwoId));
        super.logger.logMessage("Sending LocalWeightUpdateRequest which should also "
                + "send PeerWeightUpdateRequest to peerTwo to keep weights consistent on both ends");
        weighter.tell(new LocalWeightUpdateRequest(peerTwoId, new Weight(6.0)), getSelf());
        Thread.sleep(1000);
        super.logger.logMessage("Checking Weight has been updated locally");
        weighter.tell(new WeightRequest(peerTwoId), getSelf());
    }
}
