package peer.data.messages;

import content.view.core.ContentView;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Returns a ContentView from the backed up content view history
 *
 */
public class BackedUpContentViewResponse extends ActorMessage {
    private ContentView contentView;
    
    public BackedUpContentViewResponse(ContentView contentView) {
        super(ActorMessageType.BackedUpContentViewResponse);
    }
    
    public ContentView getContentView() {
        return this.contentView;
    }
}
