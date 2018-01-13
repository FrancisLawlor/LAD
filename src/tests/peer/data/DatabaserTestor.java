package tests.peer.data;

import java.util.Iterator;

import akka.actor.ActorSelection;
import content.frame.core.Content;
import content.similarity.core.SimilarContentViewPeers;
import content.similarity.core.SimilarViewPeers;
import content.view.core.ContentView;
import content.view.core.ViewingTime;
import peer.data.messages.BackedUpContentViewHistoryRequest;
import peer.data.messages.BackedUpContentViewResponse;
import peer.data.messages.BackedUpPeerLinkResponse;
import peer.data.messages.BackedUpPeerLinksRequest;
import peer.data.messages.BackedUpSimilarContentViewPeersRequest;
import peer.data.messages.BackedUpSimilarContentViewPeersResponse;
import peer.data.messages.BackupContentViewInHistoryRequest;
import peer.data.messages.BackupPeerLinkRequest;
import peer.data.messages.BackupSimilarContentViewPeersRequest;
import peer.frame.core.ActorPaths;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import peer.graph.core.PeerWeightedLink;
import peer.graph.core.Weight;
import tests.core.DummyActor;
import tests.core.DummyInit;
import tests.core.StartTest;

public class DatabaserTestor extends DummyActor {
    private int testNum = 1;
    
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
            switch(testNum) {
            case 1:
                this.testBackingUpPeerLinks();
                break;
            case 2:
                this.testBackingUpSimilarContentViewPeers();
                break;
            case 3:
                this.testBackingUpContentViewHistory();
                break;
            case 4:
                break;
            case 5:
                break;
            }
            testNum++;
        }
        else if (message instanceof BackedUpPeerLinkResponse) {
            BackedUpPeerLinkResponse response = (BackedUpPeerLinkResponse) message;
            super.logger.logMessage("Received BackedUpPeerLinkResponse in DatabaserTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Peer Link: " + response.getPeerWeightedLink().getLinkedPeerId().toString());
            super.logger.logMessage("Weight: " + response.getPeerWeightedLink().getLinkWeight().getWeight());
            super.logger.logMessage("");
        }
        else if (message instanceof BackedUpSimilarContentViewPeersResponse) {
            BackedUpSimilarContentViewPeersResponse response = (BackedUpSimilarContentViewPeersResponse) message;
            super.logger.logMessage("Received BackedSimilarContentViewPeersResponse in DatabaserTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Content: " + response.getSimilarContentViewPeers().getContent().getId());
            Iterator<UniversalId> peers = response.getSimilarContentViewPeers().getSimilarViewPeers().iterator();
            while (peers.hasNext()) {
                UniversalId peerId = peers.next();
                super.logger.logMessage("Peers who watched: " + peerId.toString());
            }
            super.logger.logMessage("");
            
        }
        else if (message instanceof BackedUpContentViewResponse) {
            BackedUpContentViewResponse response = (BackedUpContentViewResponse) message;
            super.logger.logMessage("Received BackedUpContentViewResponse in DatabaserTestor");
            super.logger.logMessage("Type: " + response.getType().toString());
            super.logger.logMessage("Content: " + response.getContentView().getContent().getId());
            super.logger.logMessage("Score: " + response.getContentView().getScore());
            super.logger.logMessage("");
        }
    }
    
    protected void testBackingUpPeerLinks() {
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        PeerWeightedLink peerWeightedLink = new PeerWeightedLink(new UniversalId("PeerOne"), new Weight(10));
        BackupPeerLinkRequest request = new BackupPeerLinkRequest(peerWeightedLink);
        databaser.tell(request, getSelf());
        peerWeightedLink = new PeerWeightedLink(new UniversalId("PeerTwo"), new Weight(2));
        request = new BackupPeerLinkRequest(peerWeightedLink);
        databaser.tell(request, getSelf());
        peerWeightedLink = new PeerWeightedLink(new UniversalId("PeerThree"), new Weight(3));
        request = new BackupPeerLinkRequest(peerWeightedLink);
        databaser.tell(request, getSelf());
        peerWeightedLink = new PeerWeightedLink(new UniversalId("PeerFour"), new Weight(4));
        request = new BackupPeerLinkRequest(peerWeightedLink);
        databaser.tell(request, getSelf());
        peerWeightedLink = new PeerWeightedLink(new UniversalId("PeerFive"), new Weight(5));
        request = new BackupPeerLinkRequest(peerWeightedLink);
        databaser.tell(request, getSelf());
        peerWeightedLink = new PeerWeightedLink(new UniversalId("PeerOne"), new Weight(1));
        request = new BackupPeerLinkRequest(peerWeightedLink);
        databaser.tell(request, getSelf());
        BackedUpPeerLinksRequest getAllBackRequest = new BackedUpPeerLinksRequest();
        databaser.tell(getAllBackRequest, getSelf());
    }
    
    protected void testBackingUpSimilarContentViewPeers() {
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        Content content = new Content("1", "1", "1", 1);
        SimilarViewPeers similarViewPeers = new SimilarViewPeers();
        similarViewPeers.add(new UniversalId("PeerFour"));
        similarViewPeers.add(new UniversalId("PeerFive"));
        SimilarContentViewPeers similarContentViewPeers = new SimilarContentViewPeers(content, similarViewPeers);
        BackupSimilarContentViewPeersRequest request = new BackupSimilarContentViewPeersRequest(similarContentViewPeers);
        databaser.tell(request, getSelf());
        content = new Content("2", "2", "2", 2);
        similarViewPeers = new SimilarViewPeers();
        similarViewPeers.add(new UniversalId("PeerSix"));
        similarViewPeers.add(new UniversalId("PeerSeven"));
        similarContentViewPeers = new SimilarContentViewPeers(content, similarViewPeers);
        request = new BackupSimilarContentViewPeersRequest(similarContentViewPeers);
        databaser.tell(request, getSelf());
        content = new Content("3", "3", "3", 3);
        similarViewPeers = new SimilarViewPeers();
        similarViewPeers.add(new UniversalId("PeerEight"));
        similarViewPeers.add(new UniversalId("PeerNine"));
        similarContentViewPeers = new SimilarContentViewPeers(content, similarViewPeers);
        request = new BackupSimilarContentViewPeersRequest(similarContentViewPeers);
        databaser.tell(request, getSelf());
        BackedUpSimilarContentViewPeersRequest getAllBackRequest = new BackedUpSimilarContentViewPeersRequest();
        databaser.tell(getAllBackRequest, getSelf());
    }
    
    protected void testBackingUpContentViewHistory() {
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        Content content = new Content("1", "1", "1", 1);
        ContentView contentView = new ContentView(content, super.peerId);
        contentView.recordView(new ViewingTime(1));
        BackupContentViewInHistoryRequest request = new BackupContentViewInHistoryRequest(contentView);
        databaser.tell(request, getSelf());
        content = new Content("2", "2", "2", 2);
        contentView = new ContentView(content, super.peerId);
        contentView.recordView(new ViewingTime(2));
        request = new BackupContentViewInHistoryRequest(contentView);
        databaser.tell(request, getSelf());
        content = new Content("3", "3", "3", 3);
        contentView = new ContentView(content, super.peerId);
        contentView.recordView(new ViewingTime(3));
        request = new BackupContentViewInHistoryRequest(contentView);
        databaser.tell(request, getSelf());
        BackedUpContentViewHistoryRequest historyRequest = new BackedUpContentViewHistoryRequest();
        databaser.tell(historyRequest, getSelf());
    }
}
