package tests.gui.core;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import peer.communicate.actors.InboundCommunicator;
import peer.communicate.messages.OutboundCommInit;
import peer.frame.core.ActorNames;
import peer.frame.core.ActorPaths;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerActorInit;
import peer.graph.core.Weight;
import peer.graph.messages.PeerWeightedLinkAddition;
import tests.core.TestPeerToPeerActorSystem;

public class TestPeerToPeerActorSystem2 extends TestPeerToPeerActorSystem {
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
        inboundCommunicator.tell(inboundInit, ActorRef.noSender());
        
        this.camelContext = getCamelContext(inboundCommunicator);
        
        final ActorRef outboundCommunicator = this.actorSystem.actorOf(Props.create(DummyOutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        PeerToPeerActorInit outboundInit = new PeerToPeerActorInit(peerId, ActorNames.OUTBOUND_COMM);
        OutboundCommInit outboundCommInit = new OutboundCommInit(camelContext);
        outboundCommunicator.tell(outboundInit, ActorRef.noSender());
        outboundCommunicator.tell(outboundCommInit, ActorRef.noSender());
        
        this.camelContext.start();
    }
    
    @Override
    protected void initialiseDatabase() {
        final ActorRef databaser = this.actorSystem.actorOf(Props.create(DummyDatabaser.class), ActorNames.DATABASER);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerId, ActorNames.INBOUND_COMM);
        databaser.tell(init, ActorRef.noSender());
    }
    
    protected void addAFakePeer() {
        ActorSelection peerLinker = this.actorSystem.actorSelection(ActorPaths.getPathToPeerLinker());
        PeerWeightedLinkAddition link = new PeerWeightedLinkAddition(new UniversalId("localhost:10003"), new Weight(20));
        peerLinker.tell(link, ActorRef.noSender());
    }
}
