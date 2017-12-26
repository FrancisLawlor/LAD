package tests.content.retrieve;

import akka.actor.ActorRef;
import content.core.GossipContentRequest;
import content.core.GossipContentResponse;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyGossiper extends DummyActor {
    private UniversalId peerThreeId = new UniversalId("localhost:10003");
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof GossipContentRequest) {
            GossipContentRequest request = (GossipContentRequest) message;
            super.logger.logMessage("GossipContentRequest received in Gossiper");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("Content ID: " + request.getContent().getId());
            super.logger.logMessage("Content Name: " + request.getContent().getFileName());
            super.logger.logMessage("Content Type: " + request.getContent().getFileFormat());
            super.logger.logMessage("Content Length: " + request.getContent().getViewLength());
            super.logger.logMessage("");
            
            super.logger.logMessage("Sending back a Gossip Response indicating the file might be at peer3 (" + peerThreeId + ")");
            super.logger.logMessage("");
            GossipContentResponse response = new GossipContentResponse(request.getContent(), peerThreeId);
            ActorRef sender = getSender();
            sender.tell(response, getSelf());
        }
    }
}
