package content.retrieve;

import content.core.Content;
import peer.core.ActorMessageType;
import peer.core.PeerToPeerRequest;
import peer.core.UniversalId;

public class LocalRetrieveContentRequest extends PeerToPeerRequest {
    private Content content;
    
    public LocalRetrieveContentRequest(UniversalId originatingPeer, UniversalId targetPeer, Content content) {
        super(ActorMessageType.LocalRetrieveContentRequest, originatingPeer, targetPeer);
        this.content = content;
    }
    
    public Content getContent() {
        return this.content;
    }
}
