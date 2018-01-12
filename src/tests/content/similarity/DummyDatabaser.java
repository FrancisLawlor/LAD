package tests.content.similarity;

import java.util.Iterator;

import peer.data.messages.BackupSimilarContentViewPeersRequest;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;

public class DummyDatabaser extends DummyActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof BackupSimilarContentViewPeersRequest) {
            BackupSimilarContentViewPeersRequest request = (BackupSimilarContentViewPeersRequest) message;
            String log =            "BackupSimilarContentViewPeersRequest received in Databaser" + "\n" +
                                    "Type: " + request.getType().toString() + "\n" +
                                    "SimilarContent: " + request.getSimilarContentViewPeers().getContent().getId() + "\n";
            Iterator<UniversalId> iterator = request.getSimilarContentViewPeers().getSimilarViewPeers().iterator();
            while (iterator.hasNext()) {
                UniversalId peerId = iterator.next();
                log += "Peer who watched content: " + peerId.toString() + "\n";
            }
            log += "\n";
            super.logger.logMessage(log);
        }
    }
}
