package peer.communicate;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

import com.google.gson.Gson;

import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import core.xcept.UnknownMessageException;
import peer.graph.weight.PeerWeightUpdateRequest;

/**
 * Communicator Actor that sends messages Outbound to another Peer with Apache Camel
 * Converts Actor Messages to JSON to be sent by REST to a RESTlet on another peer
 * Sends JSON formatted messages to another peer's RESTlet with Apache Camel client template
 *
 */
public class OutboundCommunicator extends PeerToPeerActor {
    private CamelContext camelContext;
    private ProducerTemplate camelTemplate;
    private Gson gson;
    
    public OutboundCommunicator() {
        this.gson = new Gson();
    }
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        if (message instanceof OutboundCommInit) {
            OutboundCommInit init = (OutboundCommInit) message;
            this.initialise(init);
        }
        else if (message instanceof PeerRecommendationRequest) {
            PeerRecommendationRequest request = (PeerRecommendationRequest) message;
            this.processPeerRecommendationRequest(request);
        }
        else if (message instanceof PeerRecommendation) {
            PeerRecommendation recommendation = (PeerRecommendation) message;
            this.processPeerRecommendation(recommendation);
        }
        else if (message instanceof PeerRetrieveContentRequest) {
            PeerRetrieveContentRequest contentRequest = (PeerRetrieveContentRequest) message;
            this.processPeerRetrieveContentRequest(contentRequest);
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent content = (RetrievedContent) message;
            this.processRetrievedContent(content);
        }
        else if (message instanceof PeerWeightUpdateRequest) {
            PeerWeightUpdateRequest request = (PeerWeightUpdateRequest) message;
            this.processPeerWeightUpdateRequest(request);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Initialise the OutBound Communicator with Apache Camel implementation
     * @param init
     */
    protected void initialise(OutboundCommInit init) {
        this.camelContext = init.getCamelContext();
        this.camelTemplate = this.camelContext.createProducerTemplate();
    }
    
    /**
     * Send a Recommendation Request to a peer
     * @param request
     */
    protected void processPeerRecommendationRequest(PeerRecommendationRequest request) {
        UniversalId peerId = request.getOriginalTarget();
        String restletUri = CamelURIFormatter.getPeerRestletUri(peerId);
        String requestJson = this.gson.toJson(request);
        this.camelTemplate.sendBody(restletUri, requestJson);
    }
    
    /**
     * Send a recommendation from the target peer back to the original requesting peer
     * @param recommendation
     */
    protected void processPeerRecommendation(PeerRecommendation recommendation) {
        UniversalId peerId = recommendation.getOriginalTarget();
        String restletUri = CamelURIFormatter.getPeerRestletUri(peerId);
        String requestJson = this.gson.toJson(recommendation);
        this.camelTemplate.sendBody(restletUri, requestJson);
    }
    
    /**
     * Send a content request to a target peer from this peer, the requesting peer
     * @param contentRequest
     */
    protected void processPeerRetrieveContentRequest(PeerRetrieveContentRequest contentRequest) {
        UniversalId peerId = contentRequest.getOriginalTarget();
        String restletUri = CamelURIFormatter.getPeerRestletUri(peerId);
        String requestJson = this.gson.toJson(contentRequest);
        this.camelTemplate.sendBody(restletUri, requestJson);
    }
    
    /**
     * Send the content this peer's retriever has found back to the original requesting peer
     * @param content
     */
    protected void processRetrievedContent(RetrievedContent content) {
        UniversalId peerId = content.getOriginalTarget();
        String restletUri = CamelURIFormatter.getPeerRestletUri(peerId);
        String requestJson = this.gson.toJson(content);
        this.camelTemplate.sendBody(restletUri, requestJson);
    }
    
    /**
     * Sends an outbound request from this peer to a target peer
     * Asks a target peer to update their weight with this peer to keep the weighted link consistent on both ends
     * @param request
     */
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest request) {
        UniversalId peerId = request.getOriginalTarget();
        String restletUri = CamelURIFormatter.getPeerRestletUri(peerId);
        String requestJson = this.gson.toJson(request);
        this.camelTemplate.sendBody(restletUri, requestJson);
    }

}
