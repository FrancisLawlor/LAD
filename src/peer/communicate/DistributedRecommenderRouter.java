package peer.communicate;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Describes Route information for Apache Camel
 * Tells Apache Camel what component and protocol to use
 * Tells Apache Camel what IP and port to run component on listening for messages
 * Takes in processors for each potential message type
 * Processors process REST messages back into Actor messages
 *
 */
public class DistributedRecommenderRouter extends RouteBuilder {
    private String component;
    private String protocol;
    private String ipAndPort;
    private Processor peerRecommendationRequestProcessor;
    private Processor peerRecommendationProcessor;
    private Processor peerRetrieveContentRequestProcessor;
    private Processor retrievedContentProcessor;
    private Processor peerWeightUpdateRequestProcessor;
    
    public DistributedRecommenderRouter(
            String ip, 
            String port, 
            Processor peerRecommendationRequestProcessor, 
            Processor peerRecommendationProcessor, 
            Processor peerRetrieveContentRequestProcessor, 
            Processor retrievedContentProcessor, 
            Processor peerWeightUpdateRequestProcessor) {
        this.component = "restlet:";
        this.protocol = "http://";
        this.ipAndPort = ip + ":" + port;
        this.peerRecommendationRequestProcessor = peerRecommendationRequestProcessor;
        this.peerRecommendationProcessor = peerRecommendationProcessor;
        this.peerRetrieveContentRequestProcessor = peerRetrieveContentRequestProcessor;
        this.retrievedContentProcessor = retrievedContentProcessor;
        this.peerWeightUpdateRequestProcessor = peerWeightUpdateRequestProcessor;
    }
    
    @Override
    public void configure() {
        String fromStr = component + protocol + ipAndPort;
        
        from(fromStr + "/PeerRecommendationRequest?restletMethod=post")
        .process(this.peerRecommendationRequestProcessor);
        
        from(fromStr + "/PeerRecommendation?restletMethod=post")
        .process(this.peerRecommendationProcessor);
        
        from(fromStr + "/PeerRetrieveContentRequest?restletMethod=post")
        .process(this.peerRetrieveContentRequestProcessor);
        
        from(fromStr + "/RetrievedContent?restletMethod=post")
        .process(this.retrievedContentProcessor);
        
        from(fromStr + "/PeerWeightUpdateRequest?restletMethod=post")
        .process(this.peerWeightUpdateRequestProcessor);
    }
}
