package peer.communicate;

import org.apache.camel.CamelContext;

import core.ActorMessage;
import core.ActorMessageType;

/**
 * Initialises the OutBound Communicator
 * Provides a Camel Context to an OutBound Communicator Actor
 * Provides this user's PeerID
 *
 */
public class OutboundCommInit extends ActorMessage {
    private CamelContext camelContext;
    
    public OutboundCommInit(CamelContext camelContext) {
        super(ActorMessageType.OutboundCommInit);
        this.camelContext = camelContext;
    }
    
    public CamelContext getCamelContext() {
        return this.camelContext;
    }
}
