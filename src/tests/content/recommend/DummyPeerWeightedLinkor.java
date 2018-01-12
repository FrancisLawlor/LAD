package tests.content.recommend;

import akka.actor.ActorRef;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import peer.graph.core.Weight;
import peer.graph.messages.PeerLinkResponse;
import peer.graph.messages.PeerLinksRequest;
import peer.graph.messages.WeightRequest;
import peer.graph.messages.WeightResponse;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyPeerWeightedLinkor extends DummyActor {
    private static final String PEER_TWO = "Peer2";
    private static final String PEER_THREE = "Peer3";
    private static final String PEER_FOUR = "Peer4";
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof PeerLinksRequest) {
            super.logger.logMessage("PeerLinksRequest received");
            ActorRef sender = getSender();

            super.logger.logMessage("Sending " + PEER_TWO + " as Linked Peer ID");
            UniversalId peerLinksId = new UniversalId(PEER_TWO);
            PeerLinkResponse response = new PeerLinkResponse(peerLinksId);
            sender.tell(response, getSelf());

            super.logger.logMessage("Sending " + PEER_THREE + " as Linked Peer ID");
            peerLinksId = new UniversalId(PEER_THREE);
            response = new PeerLinkResponse(peerLinksId);
            sender.tell(response, getSelf());

            super.logger.logMessage("Sending " + PEER_FOUR + " as Linked Peer ID");
            peerLinksId = new UniversalId(PEER_FOUR);
            response = new PeerLinkResponse(peerLinksId);
            sender.tell(response, getSelf());
            
            super.logger.logMessage("\n");
        }
        else if (message instanceof WeightRequest) {
            WeightRequest weightRequest = (WeightRequest) message;
            UniversalId linkedPeerId = weightRequest.getLinkedPeerId();
            super.logger.logMessage("WeightRequest received at PeerWeightedLinkor");
            Weight linkWeight = new Weight(1.0);
            if (weightRequest.getLinkedPeerId().toString().equals(PEER_TWO)) {
                linkWeight = new Weight(2.0);
            }
            else if (weightRequest.getLinkedPeerId().toString().equals(PEER_THREE)) {
                linkWeight = new Weight(3.0);
            }
            else if (weightRequest.getLinkedPeerId().toString().equals(PEER_FOUR)) {
                linkWeight = new Weight(4.0);
            }
            WeightResponse response = new WeightResponse(linkedPeerId, linkWeight);
            super.logger.logMessage("Sending WeightResponse of weight: "+ linkWeight.getWeight() + " representing weight of link between " + super.peerId.toString() + " and " + linkedPeerId.toString() + "\n");
            ActorRef sender = getSender();
            sender.tell(response, getSelf());
        }
    }
}
