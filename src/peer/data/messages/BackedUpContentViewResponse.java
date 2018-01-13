package peer.data.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Returns a ContentView from the backed up content view history
 *
 */
public class BackedUpContentViewResponse extends ActorMessage {
    public BackedUpContentViewResponse() {
        super(ActorMessageType.BackedUpContentViewResponse);
    }
}
