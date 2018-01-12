package tests.peer.communicate;

import java.util.LinkedList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import content.core.Content;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.retrieve.LocalRetrieveContentRequest;
import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import peer.communicate.DistributedRecommenderRouter;
import peer.communicate.InboundCommunicator;
import peer.communicate.OutboundCommInit;
import peer.communicate.OutboundCommunicator;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.graph.distributedmap.RemotePeerWeightedLinkAddition;
import peer.graph.weight.PeerWeightUpdateRequest;
import peer.graph.weight.Weight;

public class TestCommunicatorsSideSend {
    public static final String IP = "localhost";
    public static final String PORT = "10001";
    private static final UniversalId peerOneId = new UniversalId(IP + ":" + PORT);
    private static final UniversalId peerTwoId = new UniversalId(TestCommunicatorsSideReceive.IP + ":" + TestCommunicatorsSideReceive.PORT);
    
    private static final Content toRetrieve = new Content("UIDA", "filenameA", "filetypeA", 10);
    
    public static void main(String[] args) throws Exception {
        
        // Create Actors
        ActorSystem actorSystem = ActorSystem.create("ContentSystem");
        final ActorRef outboundComm = actorSystem.actorOf(Props.create(OutboundCommunicator.class), ActorNames.OUTBOUND_COMM);
        final ActorRef inboundComm = actorSystem.actorOf(Props.create(InboundCommunicator.class), ActorNames.INBOUND_COMM);
        
        // Initialise PeerId and Name
        PeerToPeerActorInit outboundCommInitPeerId = new PeerToPeerActorInit(peerOneId, ActorNames.OUTBOUND_COMM);
        outboundComm.tell(outboundCommInitPeerId, ActorRef.noSender());
        PeerToPeerActorInit inboundCommInitPeerId = new PeerToPeerActorInit(peerOneId, ActorNames.INBOUND_COMM);
        inboundComm.tell(inboundCommInitPeerId, ActorRef.noSender());
        
        //Initialise Apache Camel with Routes
        CamelContext camelContext = getCamelContext(inboundComm);
        
        // Specific OutboundCommunicator Initialisation with camelContext
        OutboundCommInit outboundCommInit = new OutboundCommInit(camelContext);
        outboundComm.tell(outboundCommInit, ActorRef.noSender());
        
        // Start Camel Context
        camelContext.start();
        
        // Begin test with messages after 5 seconds
        Thread.sleep(5000);
        outboundComm.tell(getPeerRecommendationRequest(), ActorRef.noSender());
        outboundComm.tell(getPeerRecommendation(), ActorRef.noSender());
        outboundComm.tell(getPeerRetrieveContentRequest(), ActorRef.noSender());
        outboundComm.tell(getRetrievedContent(), ActorRef.noSender());
        outboundComm.tell(getRemotePeerWeightedLinkAddition(), ActorRef.noSender());
        outboundComm.tell(getPeerWeightUpdateRequest(), ActorRef.noSender());
    }
    
    private static CamelContext getCamelContext(ActorRef inboundComm) throws Exception {
        CamelContext camelContext = new DefaultCamelContext();
        DistributedRecommenderRouter router = new DistributedRecommenderRouter( peerOneId, inboundComm);
        camelContext.addRoutes(router);
        return camelContext;
    }
    
    private static PeerRecommendationRequest getPeerRecommendationRequest() {
        return new PeerRecommendationRequest(peerOneId, peerTwoId);
    }
    
    private static PeerRecommendation getPeerRecommendation() {
        return new PeerRecommendation(getContent(), peerTwoId, peerOneId);
    }
    
    private static PeerRetrieveContentRequest getPeerRetrieveContentRequest() {
        LocalRetrieveContentRequest request = new LocalRetrieveContentRequest(peerOneId, peerTwoId, toRetrieve);
        return new PeerRetrieveContentRequest(request);
    }
    
    private static RetrievedContent getRetrievedContent() {
        return new RetrievedContent(peerTwoId, peerOneId, toRetrieve, null);
    }
    
    private static PeerWeightUpdateRequest getPeerWeightUpdateRequest() {
        return new PeerWeightUpdateRequest(peerOneId, peerTwoId, new Weight(10.0));
    }
    
    private static RemotePeerWeightedLinkAddition getRemotePeerWeightedLinkAddition() {
        return new RemotePeerWeightedLinkAddition(peerOneId, peerTwoId, new Weight(10.0));
    }
    
    private static List<Content> getContent() {
        List<Content> contentList = new LinkedList<Content>();
        for (int i = 1; i < 11; i++) {
            contentList.add(new Content("UID_" + i, "filename_" + i, "filetype_" + i, i));
        }
        return contentList;
    }
}

