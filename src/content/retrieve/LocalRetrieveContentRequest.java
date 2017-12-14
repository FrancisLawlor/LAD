package content.retrieve;

import content.content.Content;
import core.RequestCommunication;
import core.UniversalId;

public class LocalRetrieveContentRequest extends RequestCommunication {
    public LocalRetrieveContentRequest(UniversalId originatingPeer, UniversalId targetPeer, Content content) {
        super(originatingPeer, targetPeer);
    }
}
