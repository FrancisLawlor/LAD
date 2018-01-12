package peer.communicate.processing;

import akka.actor.ActorRef;
import content.recommend.messages.PeerRecommendation;

/**
 * Actor that doubles as an Apache Camel Processor for JSON messages
 * Converts JSON to PeerRetrieveContentRequest Actor Message
 * Sends this message to InboundCommunicator
 *
 */
public class PeerRecommendationProcessor extends JsonProcessor {
    private PeerRecommendation peerRecommendation;
    
    public PeerRecommendationProcessor(ActorRef inboundCommunicator) {
        super(inboundCommunicator);
    }
    
    protected void processSpecificMessage(String exchangeMessage) {
        this.peerRecommendation = super.gson.fromJson(exchangeMessage, PeerRecommendation.class);
        super.inboundCommunicator.tell(this.peerRecommendation, null);
    }
}
