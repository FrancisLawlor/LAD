package content.retrieve;

import content.impl.Content;
import core.ActorMessageType;
import core.PeerToPeerRequest;
import core.UniversalId;

public class LocalRetrieveContentRequest extends PeerToPeerRequest {
    public LocalRetrieveContentRequest(UniversalId originatingPeer, UniversalId targetPeer, Content content) {
        super(ActorMessageType.LocalRetrieveContentRequest, originatingPeer, targetPeer);
    }
}
