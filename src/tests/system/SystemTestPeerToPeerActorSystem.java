package tests.system;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import peer.frame.core.ActorPaths;
import peer.frame.core.PeerToPeerActorSystem;
import peer.graph.core.PeerWeightedLink;
import peer.graph.messages.PeerWeightedLinkAddition;

public class SystemTestPeerToPeerActorSystem extends PeerToPeerActorSystem {
    protected void addPeer(PeerWeightedLink peerWeightedLink) {
        ActorSelection peerLinker = this.actorSystem.actorSelection(ActorPaths.getPathToPeerLinker());
        PeerWeightedLinkAddition link = new PeerWeightedLinkAddition(peerWeightedLink.getLinkedPeerId(), peerWeightedLink.getLinkWeight());
        peerLinker.tell(link, ActorRef.noSender());
    }
}
