package peer.communicate;

import org.apache.camel.Exchange;

import akka.actor.ActorSelection;
import content.retrieve.PeerRetrieveContentRequest;
import core.ActorNames;

/**
 * Actor that doubles as an Apache Camel Processor for JSON messages
 * Converts JSON to PeerRetrieveContentRequest Actor Message
 * Sends this message to InboundCommunicator
 *
 */
public class PeerRetrieveContentRequestProcessor extends JsonProcessorActor {
    private PeerRetrieveContentRequest retrieveContentRequest;
    
    public void process(Exchange exchange) {
        try {
            String exchangeMessage = exchange.getIn().getBody().toString();
            retrieveContentRequest = super.gson.fromJson(exchangeMessage, PeerRetrieveContentRequest.class);
            
            ActorSelection inboundComm = getContext().actorSelection("user/" + ActorNames.INBOUND_COMM);
            inboundComm.tell(retrieveContentRequest, getSelf());
            
            exchange.getOut().setBody(SUCCESS + super.peerId);
        }
        catch (Exception e) {
            exchange.getOut().setBody(FAIL + super.peerId);
        }
    }
}
