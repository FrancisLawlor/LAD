package peer.communicate;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import peer.core.UniversalId;

/**
 * Describes Route information for Apache Camel
 * Tells Apache Camel what component and protocol to use
 * Tells Apache Camel what IP and port to run component on listening for messages
 * Takes in processors for each potential message type
 * Processors process REST messages back into Actor messages
 *
 */
public class DistributedRecommenderRouter extends RouteBuilder {
    private UniversalId peerId;
    private Processor peerRecommendationRequestProcessor;
    private Processor peerRecommendationProcessor;
    private Processor peerRetrieveContentRequestProcessor;
    private Processor retrievedContentProcessor;
    private Processor peerWeightUpdateRequestProcessor;
    
    public DistributedRecommenderRouter(
            UniversalId peerId,
            PeerRecommendationRequestProcessor peerRecommendationRequestProcessor, 
            PeerRecommendationProcessor peerRecommendationProcessor, 
            PeerRetrieveContentRequestProcessor peerRetrieveContentRequestProcessor, 
            RetrievedContentProcessor retrievedContentProcessor, 
            PeerWeightUpdateRequestProcessor peerWeightUpdateRequestProcessor) {
        this.peerId = peerId;
        this.peerRecommendationRequestProcessor = peerRecommendationRequestProcessor;
        this.peerRecommendationProcessor = peerRecommendationProcessor;
        this.peerRetrieveContentRequestProcessor = peerRetrieveContentRequestProcessor;
        this.retrievedContentProcessor = retrievedContentProcessor;
        this.peerWeightUpdateRequestProcessor = peerWeightUpdateRequestProcessor;
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
    }
}
