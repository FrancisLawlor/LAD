package peer.communicate;

import org.apache.camel.Exchange;

import akka.actor.ActorSelection;
import core.ActorNames;
import peer.graph.weight.PeerWeightUpdateRequest;

/**
 * Actor that doubles as an Apache Camel Processor for JSON messages
 * Converts JSON to PeerWeightUpdateRequest Actor Message
 * Sends this message to InboundCommunicator
 *
 */
public class PeerWeightUpdateRequestProcessor extends JsonProcessorActor {
    private PeerWeightUpdateRequest weightUpdateRequest;
    
    public void process(Exchange exchange) {
        try {
            String exchangeMessage = exchange.getIn().getBody().toString();
            weightUpdateRequest = super.gson.fromJson(exchangeMessage, PeerWeightUpdateRequest.class);
            
            ActorSelection inboundComm = getContext().actorSelection("user/" + ActorNames.INBOUND_COMM);
            inboundComm.tell(weightUpdateRequest, getSelf());
            
            exchange.getOut().setBody(SUCCESS + super.peerId);
        }
        catch (Exception e) {
            exchange.getOut().setBody(FAIL + super.peerId);
        }
    }
}
