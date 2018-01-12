package content.retrieve.messages;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.core.UniversalId;
import peer.frame.messages.PeerToPeerRequest;

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
