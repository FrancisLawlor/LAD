package tests.content.retrieve;

import akka.actor.ActorRef;
import content.similarity.messages.SimilarContentViewPeerAlert;
import content.similarity.messages.SimilarContentViewPeerRequest;
import content.similarity.messages.SimilarContentViewPeerResponse;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummySimilaritor extends DummyActor {
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
        else if (message instanceof SimilarContentViewPeerAlert) {
            SimilarContentViewPeerAlert alert = (SimilarContentViewPeerAlert) message;
            super.logger.logMessage("PeerSimilarViewAlert arrived in Similaritor" + "\n" +
                                    "Type: " + alert.getType().toString() +  "\n" + 
                                    "SimilarPeer: " + alert.getSimilarViewPeerId().toString() + "\n" +
                                    "WeightToGive: " + alert.getWeightToGive().getWeight() + "\n" +
                                    "Content ID: " + alert.getSimilarViewContent().getId() + "\n" +
                                    "Content Name: " + alert.getSimilarViewContent().getFileName() + "\n" +
                                    "Content Type: " + alert.getSimilarViewContent().getFileFormat() + "\n" +
                                    "Content Length: " + alert.getSimilarViewContent().getViewLength() + "\n"
                                    );
        }
        else if (message instanceof SimilarContentViewPeerRequest) {
            SimilarContentViewPeerRequest request = (SimilarContentViewPeerRequest) message;
            super.logger.logMessage("SimilarContentViewPeerRequest received in Similaritor");
            super.logger.logMessage("Type: " + request.getType().toString());
            super.logger.logMessage("Content ID: " + request.getContent().getId());
            super.logger.logMessage("Content Name: " + request.getContent().getFileName());
            super.logger.logMessage("Content Type: " + request.getContent().getFileFormat());
            super.logger.logMessage("Content Length: " + request.getContent().getViewLength());
            super.logger.logMessage("");
            
            super.logger.logMessage("Sending back a Similar Content View Peer Response indicating the file might be at peer3 (" + peerThreeId + ")");
            super.logger.logMessage("");
            SimilarContentViewPeerResponse response = new SimilarContentViewPeerResponse(request.getContent(), peerThreeId);
            ActorRef sender = getSender();
            sender.tell(response, getSelf());
        }
    }
}
