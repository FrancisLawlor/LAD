package tests.peer.communicate;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.recommend.Recommender;
import content.retrieve.Retriever;
import core.ActorNames;
import core.PeerToPeerActorInit;
import core.UniversalId;
import peer.communicate.DistributedRecommenderRouter;
import peer.communicate.InboundCommunicator;
import peer.communicate.PeerRecommendationProcessor;
import peer.communicate.PeerRecommendationRequestProcessor;
import peer.communicate.PeerRetrieveContentRequestProcessor;
import peer.communicate.PeerWeightUpdateRequestProcessor;
import peer.communicate.RetrievedContentProcessor;
import peer.graph.link.PeerLinker;
import tests.core.AsynchronousLogger;
import tests.core.DummyInit;

@SuppressWarnings("unused")
public class TestCommunicatorsSideReceive {
    public static final String IP = "localhost";
    public static final String PORT = "10002";
    private static final UniversalId peerTwoId = new UniversalId(IP + ":" + PORT);
    
    public static void main(String[] args) throws Exception {
        
        // Create Actors
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        final ActorRef inboundComm = actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        final ActorRef recommender = actorSystem.actorOf(Props.create(DummyRecommender.class), ActorNames.RECOMMENDER);
        final ActorRef peerLinker = actorSystem.actorOf(Props.create(DummyPeerLinker.class), ActorNames.PEER_LINKER);
        final ActorRef retriever = actorSystem.actorOf(Props.create(DummyRetriever.class), ActorNames.RETRIEVER);
        
        // Initialise PeerId and Name
        PeerToPeerActorInit inboundCommInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.INBOUND_COMM);
        inboundComm.tell(inboundCommInitPeerId, null);
        PeerToPeerActorInit recommenderInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.RECOMMENDER);
        recommender.tell(recommenderInitPeerId, null);
        PeerToPeerActorInit peerLinkerInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.PEER_LINKER);
        peerLinker.tell(peerLinkerInitPeerId, null);
        PeerToPeerActorInit retrieverInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.RETRIEVER);
        retriever.tell(retrieverInitPeerId, null);
        
        //Initialise Apache Camel with Routes
        CamelContext camelContext = getCamelContext(inboundComm);
        
        // Start Camel Context
        camelContext.start();
        
        //Initialise Dummy Actors with Logger
        final AsynchronousLogger logger = new AsynchronousLogger();
        DummyInit dummyInit = new DummyInit(logger);
        recommender.tell(dummyInit, null);
        peerLinker.tell(dummyInit, null);
        retriever.tell(dummyInit, null);
        
        // Waits 10 seconds for the test to complete
        Thread.sleep(10000);
        
        //Print out messages
        for (String message : logger) {
            System.out.println(message);
        }
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
                        peerTwoId,
                        peerRecommendationRequestProcessor, 
                        peerRecommendationProcessor, 
                        peerRetrieveContentRequestProcessor, 
                        retrievedContentProcessor, 
                        peerWeightUpdateRequestProcessor);
        camelContext.addRoutes(router);
        return camelContext;
    }
}
