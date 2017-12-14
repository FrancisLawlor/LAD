package content.retrieve;

import core.RequestCommunication;

public class PeerRetrieveContentRequest extends RequestCommunication {
    
    public PeerRetrieveContentRequest(LocalRetrieveContentRequest request) {
        super(request.getOriginalRequester(), request.getOriginalTarget());
    }
}
