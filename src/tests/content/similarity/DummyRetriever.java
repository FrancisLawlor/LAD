package tests.content.similarity;

import akka.actor.ActorSelection;
import content.frame.core.Content;
import content.similarity.messages.SimilarContentViewPeerAlert;
import content.similarity.messages.SimilarContentViewPeerRequest;
import content.similarity.messages.SimilarContentViewPeerResponse;
import content.view.core.ContentView;
import content.view.core.ViewingTime;
import peer.frame.core.ActorPaths;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class DummyRetriever extends DummyActor {
    private int testNum = 1;
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DummyInit) {
            super.logger = ((DummyInit)message).getLogger();
        }
        else if (message instanceof StartTest) {
            ActorSelection similaritor = getContext().actorSelection(ActorPaths.getPathToSimilaritor());
            switch(testNum) {
            case 1:
                this.sendSimilarContentViewPeersAlerts(similaritor);
                break;
            case 2:
                this.sendSimilarContentViewPeerRequest(similaritor, 0);
                break;
            case 3:
                this.sendSimilarContentViewPeerRequest(similaritor, 1);
                break;
            }
            testNum++;
        }
        else if (message instanceof SimilarContentViewPeerResponse) {
            SimilarContentViewPeerResponse response = (SimilarContentViewPeerResponse) message;
            super.logger.logMessage("SimilarContentViewPeerResponse received in Retriever");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Content: " + response.getContent().getId());
            super.logger.logMessage("Peer who watched content: " + response.getPeerId().toString());
            super.logger.logMessage("");
        }
    }
    
    protected void sendSimilarContentViewPeersAlerts(ActorSelection similaritor) throws Exception {
        for (int i = 0; i < 10; i++) {
            int contentNum = (i / 5);
            int viewLength = i + 1;
            String dummyField = "File" + contentNum;
            Content content = new Content(dummyField, dummyField, dummyField, viewLength);
            String dummyPeerId = "Peer" + i;
            UniversalId id = new UniversalId(dummyPeerId);
            ContentView contentView = new ContentView(content, id);
            contentView.recordView(new ViewingTime(viewLength));
            SimilarContentViewPeerAlert alert = new SimilarContentViewPeerAlert(contentView);
            similaritor.tell(alert, getSelf());
            super.logger.logMessage("Sending SimilarContentViewPeerAlert for Peer " + id.toString() + " who watched Content " + content.getId() + "\n");
            Thread.sleep(100);
        }
    }
    
    protected void sendSimilarContentViewPeerRequest(ActorSelection similaritor, int contentNum) {
        String dummyField = "File" + contentNum;
        Content content = new Content(dummyField, dummyField, dummyField, contentNum);
        SimilarContentViewPeerRequest request = new SimilarContentViewPeerRequest(content);
        similaritor.tell(request, getSelf());
    }
}
