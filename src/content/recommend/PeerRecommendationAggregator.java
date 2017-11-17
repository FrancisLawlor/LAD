package content.recommend;

import akka.actor.UntypedActor;
import graph.link.PeerLinkResponse;
import graph.weight.WeightResponse;

/**
 * Aggregates Recommendations from Peers
 * Based on theoretical weighted links between this peer and others
 * PeerLinker is asked for the Peer IDs of the links
 * Weighters corresponding to these Peer IDs are asked for their weight
 * Using Weight we weight the recommendations retrieved from peers
 *
 */
public class PeerRecommendationAggregator extends UntypedActor {
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof RecommendationsForUserRequest) {
            RecommendationsForUserRequest request = 
                    (RecommendationsForUserRequest) message;
            this.processRecommendationsForUserRequest(request);
        }
        else if (message instanceof PeerLinkResponse) {
            
        }
        else if (message instanceof WeightResponse) {
            
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    /**
     * Sends a request to the PeerLinker to get Peer IDs back from the links
     * @param request
     */
    protected void processRecommendationsForUserRequest(RecommendationsForUserRequest request) {
        this.sendPeerLinksRequest();
    }
    
    private void sendPeerLinksRequest() {
        
    }
    
    protected void processUserLink(PeerLinkResponse userLink) {
        
    }
    
    private void sendUserLinkWeightRequest() {
        
    }
    
    private void sendViewHistoryRequest() {
        
    }
}
