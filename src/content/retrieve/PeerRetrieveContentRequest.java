package content.retrieve;

import core.ActorMessageType;
import core.PeerToPeerRequest;

public class PeerRetrieveContentRequest extends PeerToPeerRequest {
    
    public PeerRetrieveContentRequest(LocalRetrieveContentRequest request) {
        super(ActorMessageType.PeerRetrieveContentRequest, request.getOriginalRequester(), request.getOriginalTarget());
    }
}
