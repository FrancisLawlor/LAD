package tests.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.actors.Recommender;
import content.recommend.messages.RecommendationsForUser;
import content.retrieve.actors.Retriever;
import content.retrieve.messages.RetrievedContent;
import content.similarity.actors.Similaritor;
import content.view.actors.ViewHistorian;
import content.view.actors.Viewer;
import content.view.messages.ViewerInit;
import peer.communicate.actors.InboundCommunicator;
import peer.communicate.actors.OutboundCommunicator;
import peer.communicate.core.DistributedRecommenderRouter;
import peer.communicate.messages.OutboundCommInit;
import peer.data.actors.Databaser;
import peer.data.messages.LoadedContent;
import peer.data.messages.LocalSavedContentResponse;
import peer.frame.core.ActorNames;
import peer.frame.core.UniversalId;
import peer.frame.core.ViewerToUIChannel;
import peer.frame.messages.PeerToPeerActorInit;
import peer.graph.actors.PeerWeightedLinkor;

/**
 * Initialises the permanent Actors for this Peer
 * Initialises the Apache Camel Communication System for this Peer
 *
 */
public class TestPeerToPeerActorSystem {
    protected UniversalId peerId;
    protected ActorSystem actorSystem;
    protected CamelContext camelContext;
    protected ViewerToUIChannel channel;
    
    /**
     * Initialises the Actor System and Camel Communication System for this Peer
     * @param args
     */
    public TestPeerToPeerActorSystem(UniversalId peerId) {
        this.peerId = peerId;
        this.actorSystem = ActorSystem.create("ContentSystem");
    }
    
    public void createActors() throws Exception {
        initialiseDatabase();
        initialiseViewingSystem();
        initialiseHistorySystem();
        initialiseSimilaritySystem();
        initialiseCommunicationSystem();
        initialisePeerGraph();
        initialiseRecommendingSystem();
        initialiseRetrievingSystem();
    }
    
    public ViewerToUIChannel getViewerChannel() {
        return this.channel;
    }
    
    protected void initialiseDatabase() {
        ActorRef databaser = this.actorSystem.actorOf(Props.create(Databaser.class), ActorNames.DATABASER);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(peerId, ActorNames.DATABASER);
        databaser.tell(peerIdInit, ActorRef.noSender());
    }
    
    protected void initialiseViewingSystem() throws Exception {
        ActorRef viewer = this.actorSystem.actorOf(Props.create(Viewer.class), ActorNames.VIEWER);
        PeerToPeerActorInit viewerActorInit = new PeerToPeerActorInit(peerId, ActorNames.VIEWER);
        viewer.tell(viewerActorInit, ActorRef.noSender());
        
        BlockingQueue<RecommendationsForUser> recommendationsQueue = new LinkedBlockingQueue<RecommendationsForUser>();
        BlockingQueue<RetrievedContent> retrievedContentQueue = new LinkedBlockingQueue<RetrievedContent>();
        BlockingQueue<LocalSavedContentResponse> savedContentQueue = new LinkedBlockingQueue<LocalSavedContentResponse>();
        BlockingQueue<LoadedContent> loadedContentQueue = new LinkedBlockingQueue<LoadedContent>();
        this.channel = new ViewerToUIChannel(this.peerId, viewer, recommendationsQueue, retrievedContentQueue, savedContentQueue, loadedContentQueue);
        
        ViewerInit viewerInit = new ViewerInit(recommendationsQueue, retrievedContentQueue, savedContentQueue, loadedContentQueue);
        viewer.tell(viewerInit, ActorRef.noSender());
    }
    
    protected void initialiseHistorySystem() {
        final ActorRef viewHistorian = this.actorSystem.actorOf(Props.create(ViewHistorian.class), ActorNames.VIEW_HISTORIAN);
        PeerToPeerActorInit viewHistorianInit = new PeerToPeerActorInit(peerId, ActorNames.VIEW_HISTORIAN);
        viewHistorian.tell(viewHistorianInit, ActorRef.noSender());
    }
    
    protected void initialiseSimilaritySystem() {
        final ActorRef similaritor = this.actorSystem.actorOf(Props.create(Similaritor.class), ActorNames.SIMILARITOR);
        PeerToPeerActorInit init = new PeerToPeerActorInit(peerId, ActorNames.SIMILARITOR);
        similaritor.tell(init, ActorRef.noSender());
    }
    
    protected void initialiseCommunicationSystem() throws Exception {
        final ActorRef inboundCommunicator = this.actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        PeerToPeerActorInit inboundInit = new PeerToPeerActorInit(peerId, ActorNames.INBOUND_COMM);
        inboundCommunicator.tell(inboundInit, ActorRef.noSender());
        
        this.camelContext = getCamelContext(inboundCommunicator);
        
        final ActorRef outboundCommunicator = this.actorSystem.actorOf(Props.create(OutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        PeerToPeerActorInit outboundInit = new PeerToPeerActorInit(peerId, ActorNames.OUTBOUND_COMM);
        OutboundCommInit outboundCommInit = new OutboundCommInit(camelContext);
        outboundCommunicator.tell(outboundInit, ActorRef.noSender());
        outboundCommunicator.tell(outboundCommInit, ActorRef.noSender());
        
        this.camelContext.start();
    }
    
    protected CamelContext getCamelContext(ActorRef inboundComm) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        DistributedRecommenderRouter router = new DistributedRecommenderRouter(peerId, inboundComm);
        camelContext.addRoutes(router);
        return camelContext;
    }
    
    protected void initialisePeerGraph() throws Exception {
        final ActorRef peerWeightedLinkor = actorSystem.actorOf(Props.create(PeerWeightedLinkor.class), ActorNames.PEER_LINKER);
        PeerToPeerActorInit peerLinkerInit = new PeerToPeerActorInit(peerId, ActorNames.PEER_LINKER);
        peerWeightedLinkor.tell(peerLinkerInit, ActorRef.noSender());
    }
    
    protected void initialiseRecommendingSystem() throws Exception {
        final ActorRef recommender = this.actorSystem.actorOf(Props.create(Recommender.class), ActorNames.RECOMMENDER);
        PeerToPeerActorInit recommenderInit = new PeerToPeerActorInit(peerId, ActorNames.RECOMMENDER);
        recommender.tell(recommenderInit, ActorRef.noSender());
    }
    
    protected void initialiseRetrievingSystem() throws Exception {
        final ActorRef retriever = this.actorSystem.actorOf(Props.create(Retriever.class), ActorNames.RETRIEVER);
        PeerToPeerActorInit retrieverInit = new PeerToPeerActorInit(peerId, ActorNames.RETRIEVER);
        retriever.tell(retrieverInit, ActorRef.noSender());
    }
}