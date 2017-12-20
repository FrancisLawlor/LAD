package peer.communicate;

import org.apache.camel.Exchange;

import akka.actor.ActorSelection;
import content.retrieve.RetrievedContent;
import core.ActorNames;

/**
 * Actor that doubles as an Apache Camel Processor for JSON messages
 * Converts JSON to RetrievedContent Actor Message
 * Sends RetrievedContent to InboundCommunicator
 *
 */
public class RetrievedContentProcessor extends JsonProcessorActor {
    private RetrievedContent retrievedContent;
    
    public void process(Exchange exchange) {
        try {
            String exchangeMessage = exchange.getIn().getBody().toString();
            retrievedContent = super.gson.fromJson(exchangeMessage, RetrievedContent.class);
            
            ActorSelection inboundComm = getContext().actorSelection("user/" + ActorNames.INBOUND_COMM);
            inboundComm.tell(retrievedContent, getSelf());
            
            exchange.getOut().setBody(SUCCESS + super.peerId);
        }
        catch (Exception e) {
            exchange.getOut().setBody(FAIL + super.peerId);
        }
    }
}
