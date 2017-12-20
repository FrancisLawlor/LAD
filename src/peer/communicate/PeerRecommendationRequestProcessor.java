package peer.communicate;

import org.apache.camel.Exchange;

import akka.actor.ActorSelection;
import content.recommend.PeerRecommendationRequest;
import core.ActorNames;

/**
 * Actor that doubles as an Apache Camel Processor for JSON messages
 * Converts JSON to PeerRetrieveContentRequest Actor Message
 * Sends this message to InboundCommunicator
 *
 */
public class PeerRecommendationRequestProcessor extends JsonProcessorActor {
    private PeerRecommendationRequest recommendRequest;
    
    public void process(Exchange exchange) {
        try {
            String exchangeMessage = exchange.getIn().getBody().toString();
            recommendRequest = super.gson.fromJson(exchangeMessage, PeerRecommendationRequest.class);
            
            ActorSelection inboundComm = getContext().actorSelection("user/" + ActorNames.INBOUND_COMM);
            inboundComm.tell(recommendRequest, getSelf());
            
            exchange.getOut().setBody(SUCCESS + super.peerId);
        }
        catch (Exception e) {
            exchange.getOut().setBody(FAIL + super.peerId);
        }
    }
}
