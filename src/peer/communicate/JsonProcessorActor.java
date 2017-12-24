package peer.communicate;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.google.gson.Gson;

import akka.actor.ActorRef;

/**
 * Superclass for Apache Camel Processors of JSON
 *
 */
public abstract class JsonProcessorActor implements Processor {
    protected static final String SUCCESS = "Message RECEIVED and Processing SUCCESSFUL";
    protected static final String FAIL = "Message RECEIVED but Processing FAILED";
    
    protected Gson gson;
    protected ActorRef inboundCommunicator;
    
    public JsonProcessorActor(ActorRef inboundCommunicator) {
        this.gson = new Gson();
        this.inboundCommunicator = inboundCommunicator;
    }
    
    /**
     * Processes the message exchange
     */
    public void process(Exchange exchange) {
        try {
            String exchangeMessage = exchange.getIn().getBody().toString();
            
            this.processSpecificMessage(exchangeMessage);
            
            exchange.getOut().setBody(SUCCESS);
        }
        catch (Exception e) {
            exchange.getOut().setBody(FAIL);
        }
    }
    
    protected abstract void processSpecificMessage(String exchangeMessage);
}
