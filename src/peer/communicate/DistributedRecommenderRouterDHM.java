package peer.communicate;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import peer.core.UniversalId;

public class DistributedRecommenderRouterDHM extends RouteBuilder {
    private UniversalId peerId;
    private Processor peerRecommendationRequestProcessor;
    private Processor peerRecommendationProcessor;
    private Processor peerRetrieveContentRequestProcessor;
    private Processor retrievedContentProcessor;
    private Processor peerWeightUpdateRequestProcessor;
    private Processor remotePeerWeightedLinkAdditionProcessor;
    
    public DistributedRecommenderRouterDHM(
            UniversalId peerId,
            PeerRecommendationRequestProcessor peerRecommendationRequestProcessor, 
            PeerRecommendationProcessor peerRecommendationProcessor, 
            PeerRetrieveContentRequestProcessor peerRetrieveContentRequestProcessor, 
            RetrievedContentProcessor retrievedContentProcessor, 
            PeerWeightUpdateRequestProcessor peerWeightUpdateRequestProcessor,
            RemotePeerWeightedLinkAdditionProcessor remotePeerWeightedLinkAddition) {
        this.peerId = peerId;
        this.peerRecommendationRequestProcessor = peerRecommendationRequestProcessor;
        this.peerRecommendationProcessor = peerRecommendationProcessor;
        this.peerRetrieveContentRequestProcessor = peerRetrieveContentRequestProcessor;
        this.retrievedContentProcessor = retrievedContentProcessor;
        this.peerWeightUpdateRequestProcessor = peerWeightUpdateRequestProcessor;
        this.remotePeerWeightedLinkAdditionProcessor = remotePeerWeightedLinkAddition;
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
