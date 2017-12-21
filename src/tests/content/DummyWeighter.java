package tests.content;

import akka.actor.ActorRef;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.graph.weight.Weight;
import peer.graph.weight.WeightRequest;
import peer.graph.weight.WeightResponse;
import peer.graph.weight.WeighterInit;

public class DummyWeighter extends DummyActor {
    private UniversalId linkedPeerId;
    private Weight linkWeight;
    
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            DummyInit init = (DummyInit) message;
            super.logger = init.getLogger();
        }
        else if (message instanceof WeighterInit) {
            WeighterInit init = (WeighterInit) message;
            this.linkedPeerId = init.getLinkedPeerId();
            this.linkWeight = init.getInitialLinkWeight();
        }
        else if (message instanceof WeightRequest) {
            WeightRequest weightRequest = (WeightRequest) message;
            this.processWeightRequest(weightRequest);
        }
    }
    
    protected void processWeightRequest(WeightRequest weightRequest) { 
        super.logger.logMessage("WeightRequest received at weighter representing link from " + super.peerId.toString() + " to " + this.linkedPeerId.toString());
        
        WeightResponse response = new WeightResponse(linkedPeerId, linkWeight);
        super.logger.logMessage("Sending WeightResponse of weight: "+ linkWeight.getWeight() + " representing weight of link between " + super.peerId.toString() + " and " + this.linkedPeerId.toString());
        ActorRef sender = getSender();
        sender.tell(response, getSelf());
    }
}
