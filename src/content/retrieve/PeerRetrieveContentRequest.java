package content.retrieve;

import core.ActorMessageType;
import core.RequestCommunication;

public class PeerRetrieveContentRequest extends RequestCommunication {
    
    public PeerRetrieveContentRequest(LocalRetrieveContentRequest request) {
        super(ActorMessageType.PeerRetrieveContentRequest, request.getOriginalRequester(), request.getOriginalTarget());
    }
}
