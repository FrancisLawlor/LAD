package peer.data.messages;

import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Request all the saved content on this peer
 *
 */
public class LocalSavedContentRequest extends ActorMessage {
    public LocalSavedContentRequest() {
        super(ActorMessageType.LocalSavedContentRequest);
    }
}
