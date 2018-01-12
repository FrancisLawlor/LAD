package peer.communicate.processing;

import akka.actor.ActorRef;
import peer.graph.messages.PeerWeightUpdateRequest;

/**
 * Actor that doubles as an Apache Camel Processor for JSON messages
 * Converts JSON to PeerWeightUpdateRequest Actor Message
 * Sends this message to InboundCommunicator
 *
 */
public class PeerWeightUpdateRequestProcessor extends JsonProcessor {
    private PeerWeightUpdateRequest weightUpdateRequest;
    
    public PeerWeightUpdateRequestProcessor(ActorRef inboundCommunicator) {
        super(inboundCommunicator);
    }
    
    protected void processSpecificMessage(String exchangeMessage) {
        this.weightUpdateRequest = super.gson.fromJson(exchangeMessage, PeerWeightUpdateRequest.class);
        super.inboundCommunicator.tell(this.weightUpdateRequest, null);
    }
}
