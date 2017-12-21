package content.retrieve;

import content.impl.Content;
import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

public class LocalRetrieveContentRequest extends RequestCommunication {
    public LocalRetrieveContentRequest(UniversalId originatingPeer, UniversalId targetPeer, Content content) {
        super(ActorMessageType.LocalRetrieveContentRequest, originatingPeer, targetPeer);
    }
}
