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
    public static final String PEER_RECOMMENDATION_REQUEST = "/PeerRecommendationRequest";
    public static final String PEER_RECOMMENDATION = "/PeerRecommendation";
    public static final String PEER_RETRIEVE_CONTENT_REQUEST = "/PeerRetrieveContentRequest";
    public static final String RETRIEVED_CONTENT = "/RetrievedContent";
    public static final String PEER_WEIGHT_UPDATE_REQUEST = "/PeerWeightUpdateRequest";
    
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
        String routedFrom = component + protocol + ipAndPort;
        String method = "?restletMethod=post";
        
        from(routedFrom + PEER_RECOMMENDATION_REQUEST + method)
        .process(this.peerRecommendationRequestProcessor);
        
        from(routedFrom + PEER_RECOMMENDATION + method)
        .process(this.peerRecommendationProcessor);
        
        from(routedFrom + PEER_RETRIEVE_CONTENT_REQUEST + method)
        .process(this.peerRetrieveContentRequestProcessor);
        
        from(routedFrom + RETRIEVED_CONTENT + method)
        .process(this.retrievedContentProcessor);
        
        from(routedFrom + PEER_WEIGHT_UPDATE_REQUEST + method)
        .process(this.peerWeightUpdateRequestProcessor);
    }
}
