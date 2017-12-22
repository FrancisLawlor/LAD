package peer.gossiper;

import core.ActorMessageType;
import core.RequestCommunication;
import core.UniversalId;

public class NewAddressForPeer extends RequestCommunication {
    public NewAddressForPeer(UniversalId originalRequester, UniversalId originalTarget) {
        super(ActorMessageType.NewAddressForPeer, originalRequester, originalTarget);
    }
}
