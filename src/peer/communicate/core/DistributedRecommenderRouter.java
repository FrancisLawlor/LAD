package peer.communicate.core;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import akka.actor.ActorRef;
import peer.communicate.processing.PeerRecommendationProcessor;
import peer.communicate.processing.PeerRecommendationRequestProcessor;
import peer.communicate.processing.PeerRetrieveContentRequestProcessor;
import peer.communicate.processing.PeerWeightUpdateRequestProcessor;
import peer.communicate.processing.RemotePeerWeightedLinkAdditionProcessor;
import peer.communicate.processing.RetrievedContentProcessor;
import peer.frame.core.UniversalId;

public class DistributedRecommenderRouter extends RouteBuilder {
    private UniversalId peerId;
    private Processor peerRecommendationRequestProcessor;
    private Processor peerRecommendationProcessor;
    private Processor peerRetrieveContentRequestProcessor;
    private Processor retrievedContentProcessor;
    private Processor peerWeightUpdateRequestProcessor;
    private Processor remotePeerWeightedLinkAdditionProcessor;
    
    public DistributedRecommenderRouter(
            UniversalId peerId,
            ActorRef inboundComm) {
        this.peerId = peerId;
        this.peerRecommendationRequestProcessor = new PeerRecommendationRequestProcessor(inboundComm);
        this.peerRecommendationProcessor = new PeerRecommendationProcessor(inboundComm);
        this.peerRetrieveContentRequestProcessor = new PeerRetrieveContentRequestProcessor(inboundComm);
        this.retrievedContentProcessor = new RetrievedContentProcessor(inboundComm);
        this.peerWeightUpdateRequestProcessor = new PeerWeightUpdateRequestProcessor(inboundComm);
        this.remotePeerWeightedLinkAdditionProcessor = new RemotePeerWeightedLinkAdditionProcessor(inboundComm);
    }
    
    @Override
    public void configure() {        
        from(CamelRestletUris.getPeerRecommendationRequest(peerId))
        .process(this.peerRecommendationRequestProcessor);
        
        from(CamelRestletUris.getPeerRecommendation(peerId))
        .process(this.peerRecommendationProcessor);
        
        from(CamelRestletUris.getPeerRetrieveContentRequest(peerId))
        .process(this.peerRetrieveContentRequestProcessor);
        
        from(CamelRestletUris.getRetrievedContent(peerId))
        .process(this.retrievedContentProcessor);
        
        from(CamelRestletUris.getPeerWeightUpdateRequest(peerId))
        .process(this.peerWeightUpdateRequestProcessor);
        
        from(CamelRestletUris.getRemotePeerWeightedLinkAddition(peerId))
        .process(this.remotePeerWeightedLinkAdditionProcessor);
    }
}
