package content.retrieve;

import content.core.Content;
import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

/**
 * Sent from one peer's retriever to another requesting content
 *
 */
public class PeerRetrieveContentRequest extends PeerToPeerRequest {
    private Content content;
    
    public PeerRetrieveContentRequest(UniversalId originatingPeer, UniversalId targetPeer, Content content) {
        super(ActorMessageType.PeerRetrieveContentRequest, originatingPeer, targetPeer);
        this.content = content;
    }
    
    public PeerRetrieveContentRequest(LocalRetrieveContentRequest request) {
        super(ActorMessageType.PeerRetrieveContentRequest, request.getOriginalRequester(), request.getOriginalTarget());
        this.content = request.getContent();
    }
    
    public Content getContent() {
        return this.content;
    }
}
