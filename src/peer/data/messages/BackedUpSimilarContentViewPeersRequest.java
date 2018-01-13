package peer.data.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Requests a SimilarContentViewPeers is returned from the Databaser backup
 *
 */
public class BackedUpSimilarContentViewPeersRequest extends ActorMessage {
    public BackedUpSimilarContentViewPeersRequest() {
        super(ActorMessageType.BackedUpSimilarContentViewPeersRequest);
    }
}
