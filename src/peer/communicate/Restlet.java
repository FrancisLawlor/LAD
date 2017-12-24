package peer.communicate;

/**
 * Constants for Restlet use with Apache Camel
 *
 */
public class Restlet {
    public static final String COMPONENT = "restlet:";
    public static final String PROTOCOL = "http://";
    public static final String METHOD = "?restletMethod=post";
    
    public static final String PEER_RECOMMENDATION_REQUEST = "/PeerRecommendationRequest";
    public static final String PEER_RECOMMENDATION = "/PeerRecommendation";
    public static final String PEER_RETRIEVE_CONTENT_REQUEST = "/PeerRetrieveContentRequest";
    public static final String RETRIEVED_CONTENT = "/RetrievedContent";
    public static final String PEER_WEIGHT_UPDATE_REQUEST = "/PeerWeightUpdateRequest";
}
