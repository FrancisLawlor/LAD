package peer.initialisation;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.Recommender;
import content.retrieve.Retriever;
import content.view.ViewHistorian;
import content.view.Viewer;
import content.view.ViewerInit;
import core.ActorNames;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.communicate.DistributedRecommenderRouter;
import peer.communicate.InboundCommunicator;
import peer.communicate.OutboundCommInit;
import peer.communicate.PeerRecommendationProcessor;
import peer.communicate.PeerRecommendationRequestProcessor;
import peer.communicate.PeerRetrieveContentRequestProcessor;
import peer.communicate.PeerWeightUpdateRequestProcessor;
import peer.communicate.RetrievedContentProcessor;
import peer.graph.link.PeerLinker;
import statemachine.core.StateMachine;

/**
 * Initialises the permanent Actors for this Peer
 * Initialises the Apache Camel Communication System for this Peer
 *
 */
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
        PeerToPeerActorInit viewerActorInit = new PeerToPeerActorInit(peerId, ActorNames.VIEWER);
        viewer.tell(viewerActorInit, null);
        ViewerInit viewerStateMachineInit = new ViewerInit(new StateMachine());
        viewer.tell(viewerStateMachineInit, null);
        
        final ActorRef viewHistorian = actorSystem.actorOf(Props.create(ViewHistorian.class), ActorNames.VIEW_HISTORIAN);
        PeerToPeerActorInit viewHistorianInit = new PeerToPeerActorInit(peerId, ActorNames.VIEW_HISTORIAN);
        viewHistorian.tell(viewHistorianInit, null);
    }
    
    private static void initialiseCommunicationSystem() throws Exception {
        final ActorRef inboundCommunicator = actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        PeerToPeerActorInit inboundInit = new PeerToPeerActorInit(peerId, ActorNames.INBOUND_COMM);
        inboundCommunicator.tell(inboundInit, null);
        
        camelContext = getCamelContext(inboundCommunicator);
        
        final ActorRef outboundCommunicator = actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        PeerToPeerActorInit outboundInit = new PeerToPeerActorInit(peerId, ActorNames.OUTBOUND_COMM);
        OutboundCommInit outboundCommInit = new OutboundCommInit(camelContext);
        outboundCommunicator.tell(outboundInit, null);
        outboundCommunicator.tell(outboundCommInit, null);
        
        camelContext.start();
    }
    
    private static CamelContext getCamelContext(ActorRef inboundComm) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        // Router and Processors for Routes initialisation
        PeerRecommendationRequestProcessor peerRecommendationRequestProcessor = 
                new PeerRecommendationRequestProcessor(inboundComm);
        PeerRecommendationProcessor peerRecommendationProcessor = 
                new PeerRecommendationProcessor(inboundComm);
        PeerRetrieveContentRequestProcessor peerRetrieveContentRequestProcessor = 
                new PeerRetrieveContentRequestProcessor(inboundComm);
        RetrievedContentProcessor retrievedContentProcessor = 
                new RetrievedContentProcessor(inboundComm);
        PeerWeightUpdateRequestProcessor peerWeightUpdateRequestProcessor = 
                new PeerWeightUpdateRequestProcessor(inboundComm);
        DistributedRecommenderRouter router = 
                new DistributedRecommenderRouter(
                        peerId,
                        peerRecommendationRequestProcessor, 
                        peerRecommendationProcessor, 
                        peerRetrieveContentRequestProcessor, 
                        retrievedContentProcessor, 
                        peerWeightUpdateRequestProcessor);
        camelContext.addRoutes(router);
        return camelContext;
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
