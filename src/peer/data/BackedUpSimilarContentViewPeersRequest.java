package peer.data;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

public class BackedUpSimilarContentViewPeersRequest extends ActorMessage {
    public BackedUpSimilarContentViewPeersRequest() {
        super(ActorMessageType.BackedUpSimilarContentViewPeersRequest);
    }
}
