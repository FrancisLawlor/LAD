package peer.initialisation;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.PeerRecommendationAggregator;
import content.recommend.Recommender;
import content.retrieve.Retriever;
import content.view.ViewHistorian;
import content.view.Viewer;
import core.ActorNames;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.communicate.InboundCommunicator;
import peer.communicate.OutboundCommInit;
import peer.graph.link.PeerLinker;

/**
 * Initialises the permanent Actors for this Peer
 * Initialises the Apache Camel Communication System for this Peer
 *
 */
@SuppressWarnings("unused")
public class Initialiser {
    private static UniversalId peerId;
    private static ActorSystem actorSystem;
    private static CamelContext camelContext;
    
    /**
     * Initialises the Actor System and Camel Communication System for this Peer
     * @param args
     */
    public static void main(String[] args) {
        peerId = initialiseUniversalId();
        
        camelContext = new DefaultCamelContext();
        actorSystem = ActorSystem.create("ContentSystem");
        
        try {
            initialiseViewingSystem();
            initialiseCommunicationSystem();
            initialisePeerGraph();
            initialiseRecommendingSystem();
            initialiseRetrievingSystem();
        } catch (Exception e) { };
    }
    
    private static UniversalId initialiseUniversalId() {
        return new UniversalId("Test");
    }
    
    /**
     * Initialise the Viewing System
     * @param actorSystem
     * @throws Exception
     */
    private static void initialiseViewingSystem() throws Exception {
        final ActorRef viewer = actorSystem.actorOf(Props.create(Viewer.class), ActorNames.VIEWER);
        PeerToPeerActorInit viewerInit = new PeerToPeerActorInit(peerId, ActorNames.VIEWER);
        viewer.tell(viewerInit, null);
        
        final ActorRef viewHistorian = actorSystem.actorOf(Props.create(ViewHistorian.class), ActorNames.VIEW_HISTORIAN);
        PeerToPeerActorInit viewHistorianInit = new PeerToPeerActorInit(peerId, ActorNames.VIEW_HISTORIAN);
        viewHistorian.tell(viewHistorianInit, null);
    }
    
    private static void initialiseCommunicationSystem() throws Exception {
        final ActorRef inboundCommunicator = actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        PeerToPeerActorInit inboundInit = new PeerToPeerActorInit(peerId, ActorNames.INBOUND_COMM);
        inboundCommunicator.tell(inboundInit, null);
        
        final ActorRef outboundCommunicator = actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        PeerToPeerActorInit outboundInit = new PeerToPeerActorInit(peerId, ActorNames.OUTBOUND_COMM);
        OutboundCommInit outboundCommInit = new OutboundCommInit(camelContext);
        outboundCommunicator.tell(outboundInit, null);
        outboundCommunicator.tell(outboundCommInit, null);
    }
    
    private static void initialisePeerGraph() throws Exception {
        final ActorRef peerLinker = actorSystem.actorOf(Props.create(PeerLinker.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit peerLinkerInit = new PeerToPeerActorInit(peerId, ActorNames.PEER_LINKER);
        peerLinker.tell(peerLinkerInit, null);
        
        // Loop over a file and tell peerLinker it's peers...
        
        // Loop over a file and create a weighter for each peer
        // While looping over file tell each weighter its weight
    }
    
    private static void initialiseRecommendingSystem() throws Exception {
        final ActorRef recommender = actorSystem.actorOf(Props.create(Recommender.class), ActorNames.RECOMMENDER);
        PeerToPeerActorInit recommenderInit = new PeerToPeerActorInit(peerId, ActorNames.RECOMMENDER);
        recommender.tell(recommenderInit, null);
    }
    
    private static void initialiseRetrievingSystem() throws Exception {
        final ActorRef retriever = actorSystem.actorOf(Props.create(Retriever.class), ActorNames.RETRIEVER);
        PeerToPeerActorInit retrieverInit = new PeerToPeerActorInit(peerId, ActorNames.RETRIEVER);
        retriever.tell(retrieverInit, null);
    }
}
