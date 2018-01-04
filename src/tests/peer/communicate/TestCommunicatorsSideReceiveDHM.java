package tests.peer.communicate;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import peer.communicate.DistributedRecommenderRouterDHM;
import peer.communicate.InboundCommunicatorDHM;
import peer.communicate.PeerRecommendationProcessor;
import peer.communicate.PeerRecommendationRequestProcessor;
import peer.communicate.PeerRetrieveContentRequestProcessor;
import peer.communicate.PeerWeightUpdateRequestProcessor;
import peer.communicate.RemotePeerWeightedLinkAdditionProcessor;
import peer.communicate.RetrievedContentProcessor;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import tests.core.ActorTestLogger;
import tests.core.DummyInit;

public class TestCommunicatorsSideReceiveDHM {
    public static final String IP = "localhost";
    public static final String PORT = "10002";
    private static final UniversalId peerTwoId = new UniversalId(IP + ":" + PORT);
    
    public static void main(String[] args) throws Exception {
        
        // Create Actors
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        final ActorRef inboundComm = actorSystem.actorOf(Props.create(InboundCommunicatorDHM.class), ActorNames.INBOUND_COMM);
        final ActorRef recommender = actorSystem.actorOf(Props.create(DummyRecommender.class), ActorNames.RECOMMENDER);
        final ActorRef peerWeightedLinkorDHM = actorSystem.actorOf(Props.create(DummyPeerWeightedLinkorDHM.class), ActorNames.PEER_LINKER);
        final ActorRef retriever = actorSystem.actorOf(Props.create(DummyRetriever.class), ActorNames.RETRIEVER);
        
        // Initialise PeerId and Name
        PeerToPeerActorInit inboundCommInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.INBOUND_COMM);
        inboundComm.tell(inboundCommInitPeerId, ActorRef.noSender());
        PeerToPeerActorInit recommenderInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.RECOMMENDER);
        recommender.tell(recommenderInitPeerId, ActorRef.noSender());
        PeerToPeerActorInit peerLinkerInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.PEER_LINKER);
        peerWeightedLinkorDHM.tell(peerLinkerInitPeerId, ActorRef.noSender());
        PeerToPeerActorInit retrieverInitPeerId = new PeerToPeerActorInit(peerTwoId, ActorNames.RETRIEVER);
        retriever.tell(retrieverInitPeerId, ActorRef.noSender());
        
        //Initialise Apache Camel with Routes
        CamelContext camelContext = getCamelContext(inboundComm);
        
        // Start Camel Context
        camelContext.start();
        
        //Initialise Dummy Actors with Logger
        final ActorTestLogger logger = new ActorTestLogger();
        DummyInit dummyInit = new DummyInit(logger);
        recommender.tell(dummyInit, ActorRef.noSender());
        peerWeightedLinkorDHM.tell(dummyInit, ActorRef.noSender());
        retriever.tell(dummyInit, ActorRef.noSender());
        
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
        RemotePeerWeightedLinkAdditionProcessor remotePeerWeightedLinkAdditionProcessor =
                new RemotePeerWeightedLinkAdditionProcessor(inboundComm);
        DistributedRecommenderRouterDHM router = 
                new DistributedRecommenderRouterDHM(
                        peerTwoId,
                        peerRecommendationRequestProcessor, 
                        peerRecommendationProcessor, 
                        peerRetrieveContentRequestProcessor, 
                        retrievedContentProcessor, 
                        peerWeightUpdateRequestProcessor,
                        remotePeerWeightedLinkAdditionProcessor);
        camelContext.addRoutes(router);
        return camelContext;
    }
}
