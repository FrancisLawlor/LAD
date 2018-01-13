package peer.data.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Requests the whole content view history is retrieved from the Databaser backup
 *
 */
public class BackedUpContentViewHistoryRequest extends ActorMessage {
    public BackedUpContentViewHistoryRequest() {
        super(ActorMessageType.BackedUpContentViewHistoryRequest);
    }
}
