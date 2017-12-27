package tests.gui.core;

import akka.actor.ActorRef;
import akka.actor.Props;
import peer.communicate.InboundCommunicator;
import peer.communicate.OutboundCommInit;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.PeerToPeerActorSystem;
import peer.core.UniversalId;

public class TestPeerToPeerActorSystem2 extends PeerToPeerActorSystem {
    public TestPeerToPeerActorSystem2(UniversalId peerId) {
        super(peerId);
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
}
