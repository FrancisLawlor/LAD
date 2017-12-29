package tests.gui.core;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import peer.communicate.InboundCommunicator;
import peer.communicate.OutboundCommInit;
import peer.core.ActorNames;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActorInit;
import peer.core.PeerToPeerActorSystem;
import peer.core.UniversalId;
import peer.graph.link.PeerLinkAddition;
import peer.graph.weight.Weight;

public class TestPeerToPeerActorSystem2 extends PeerToPeerActorSystem {
    public TestPeerToPeerActorSystem2(UniversalId peerId) {
        super(peerId);
    }
    
    @Override
    protected void initialiseHistorySystem() {
        final ActorRef viewHistorian = this.actorSystem.actorOf(Props.create(DummyViewHistorian.class), ActorNames.VIEW_HISTORIAN);
        PeerToPeerActorInit viewHistorianInit = new PeerToPeerActorInit(peerId, ActorNames.VIEW_HISTORIAN);
        viewHistorian.tell(viewHistorianInit, ActorRef.noSender());
    }
    
    @Override
    protected void initialiseCommunicationSystem() throws Exception {
        final ActorRef inboundCommunicator = this.actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        PeerToPeerActorInit inboundInit = new PeerToPeerActorInit(peerId, ActorNames.INBOUND_COMM);
        inboundCommunicator.tell(inboundInit, null);
        
        this.camelContext = getCamelContext(inboundCommunicator);
        
        final ActorRef outboundCommunicator = this.actorSystem.actorOf(Props.create(DummyOutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        PeerToPeerActorInit outboundInit = new PeerToPeerActorInit(peerId, ActorNames.OUTBOUND_COMM);
        OutboundCommInit outboundCommInit = new OutboundCommInit(camelContext);
        outboundCommunicator.tell(outboundInit, null);
        outboundCommunicator.tell(outboundCommInit, null);
        
        this.camelContext.start();
    }
    
    protected void addAFakePeer() {
        ActorSelection peerLinker = this.actorSystem.actorSelection(ActorPaths.getPathToPeerLinker());
        PeerLinkAddition link = new PeerLinkAddition(new UniversalId("localhost:10003"), new Weight(20));
        peerLinker.tell(link, ActorRef.noSender());
    }
}
