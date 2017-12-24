package peer.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.RecommendationsForUser;
import content.recommend.Recommender;
import content.retrieve.RetrievedContent;
import content.retrieve.Retriever;
import content.view.ViewHistorian;
import content.view.Viewer;
import content.view.ViewerInit;
import peer.communicate.DistributedRecommenderRouter;
import peer.communicate.InboundCommunicator;
import peer.communicate.OutboundCommInit;
import peer.communicate.OutboundCommunicator;
import peer.communicate.PeerRecommendationProcessor;
import peer.communicate.PeerRecommendationRequestProcessor;
import peer.communicate.PeerRetrieveContentRequestProcessor;
import peer.communicate.PeerWeightUpdateRequestProcessor;
import peer.communicate.RetrievedContentProcessor;
import peer.graph.link.PeerLinker;

/**
 * Initialises the permanent Actors for this Peer
 * Initialises the Apache Camel Communication System for this Peer
 *
 */
public class PeerToPeerActorSystem {
    protected UniversalId peerId;
    protected ActorSystem actorSystem;
    protected CamelContext camelContext;
    protected ViewerToUIChannel channel;
    
    /**
     * Initialises the Actor System and Camel Communication System for this Peer
     * @param args
     */
    public PeerToPeerActorSystem(UniversalId peerId) {
        this.peerId = peerId;
        this.actorSystem = ActorSystem.create("ContentSystem");
    }
    
    public void createActors() throws Exception {
        initialiseViewingSystem();
        initialiseCommunicationSystem();
        initialisePeerGraph();
        initialiseRecommendingSystem();
        initialiseRetrievingSystem();
    }
    
    public ViewerToUIChannel getViewerChannel() {
        return this.channel;
    }
    
    /**
     * Initialise the Viewing System
     * @param actorSystem
     * @throws Exception
     */
    protected void initialiseViewingSystem() throws Exception {
        ActorRef viewer = this.actorSystem.actorOf(Props.create(Viewer.class), ActorNames.VIEWER);
        PeerToPeerActorInit viewerActorInit = new PeerToPeerActorInit(peerId, ActorNames.VIEWER);
        viewer.tell(viewerActorInit, null);
        
        BlockingQueue<RecommendationsForUser> recommendationsQueue = new ArrayBlockingQueue<RecommendationsForUser>(10);
        BlockingQueue<RetrievedContent> retrievedContentQueue = new ArrayBlockingQueue<RetrievedContent>(10);
        this.channel = new ViewerToUIChannel(this.peerId, viewer, recommendationsQueue, retrievedContentQueue);
        
        ViewerInit viewerInit = new ViewerInit(recommendationsQueue, retrievedContentQueue);
        viewer.tell(viewerInit, null);
        
        final ActorRef viewHistorian = this.actorSystem.actorOf(Props.create(ViewHistorian.class), ActorNames.VIEW_HISTORIAN);
        PeerToPeerActorInit viewHistorianInit = new PeerToPeerActorInit(peerId, ActorNames.VIEW_HISTORIAN);
        viewHistorian.tell(viewHistorianInit, null);
    }
    
    protected void initialiseCommunicationSystem() throws Exception {
        final ActorRef inboundCommunicator = this.actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        PeerToPeerActorInit inboundInit = new PeerToPeerActorInit(peerId, ActorNames.INBOUND_COMM);
        inboundCommunicator.tell(inboundInit, null);
        
        this.camelContext = getCamelContext(inboundCommunicator);
        
        final ActorRef outboundCommunicator = this.actorSystem.actorOf(Props.create(OutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        PeerToPeerActorInit outboundInit = new PeerToPeerActorInit(peerId, ActorNames.OUTBOUND_COMM);
        OutboundCommInit outboundCommInit = new OutboundCommInit(camelContext);
        outboundCommunicator.tell(outboundInit, null);
        outboundCommunicator.tell(outboundCommInit, null);
        
        this.camelContext.start();
    }
    
    protected CamelContext getCamelContext(ActorRef inboundComm) throws Exception {
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
    
    protected void initialisePeerGraph() throws Exception {
        final ActorRef peerLinker = actorSystem.actorOf(Props.create(PeerLinker.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit peerLinkerInit = new PeerToPeerActorInit(peerId, ActorNames.PEER_LINKER);
        peerLinker.tell(peerLinkerInit, null);
        
        // Loop over a file and tell peerLinker it's peers...
        
        // Loop over a file and create a weighter for each peer
        // While looping over file tell each weighter its weight
    }
    
    protected void initialiseRecommendingSystem() throws Exception {
        final ActorRef recommender = this.actorSystem.actorOf(Props.create(Recommender.class), ActorNames.RECOMMENDER);
        PeerToPeerActorInit recommenderInit = new PeerToPeerActorInit(peerId, ActorNames.RECOMMENDER);
        recommender.tell(recommenderInit, null);
    }
    
    protected void initialiseRetrievingSystem() throws Exception {
        final ActorRef retriever = this.actorSystem.actorOf(Props.create(Retriever.class), ActorNames.RETRIEVER);
        PeerToPeerActorInit retrieverInit = new PeerToPeerActorInit(peerId, ActorNames.RETRIEVER);
        retriever.tell(retrieverInit, null);
    }
}
