package tests.content.similarity;

import peer.frame.messages.PeerToPeerActorInit;
import peer.graph.messages.LocalWeightUpdateRequest;
import peer.graph.messages.PeerLinkExistenceRequest;
import peer.graph.messages.PeerLinkExistenceResponse;
import peer.graph.messages.PeerWeightedLinkAddition;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyPeerWeightedLinkor extends DummyActor {
    private int existenceRequestNum = 1;
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof PeerLinkExistenceRequest) {
            PeerLinkExistenceRequest request = (PeerLinkExistenceRequest) message;
            String log =            "PeerLinkExistenceRequest received in PeerWeightedLinkor" + "\n" +
                                    "Type: " + request.getType().toString() + "\n" +
                                    "Link to check : " + request.getLinkToCheckPeerId().toString() + "\n";
            PeerLinkExistenceResponse response;
            if (existenceRequestNum < 5) {
                response = new PeerLinkExistenceResponse(request.getLinkToCheckPeerId(), false);
                log += "Responding with False\n";
            }
            else {
                response = new PeerLinkExistenceResponse(request.getLinkToCheckPeerId(), true);
                log += "Responding with True\n";
            }
            super.logger.logMessage(log);
            getSender().tell(response, getSelf());
            existenceRequestNum++;
        }
        else if (message instanceof PeerWeightedLinkAddition) {
            PeerWeightedLinkAddition addition = (PeerWeightedLinkAddition) message;
            super.logger.logMessage("PeerWeightedLinkAddition received in PeerWeightedLinkor" + "\n" + 
                                    "Type: " + addition.getType().toString() + "\n" +
                                    "Linked Peer Id: " + addition.getLinkPeerId().toString() + "\n" +
                                    "Link Weight: " + addition.getLinkWeight().getWeight() + "\n");
        }
        else if (message instanceof LocalWeightUpdateRequest) {
            LocalWeightUpdateRequest request = (LocalWeightUpdateRequest) message;
            super.logger.logMessage("LocalWeightUpdateRequest received in PeerWeightedLinkor" + "\n" +
                                    "Type: " + request.getType().toString() + "\n" +
                                    "Linked Peer Id: " + request.getLinkedPeerId().toString() + "\n" +
                                    "Link Weight: " + request.getNewWeight().getWeight() + "\n");
        }
    }
}
