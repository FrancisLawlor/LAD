package content.view.messages;

import content.view.core.ContentView;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Tells View Historian to record Content View in history
 *
 */
public class RecordContentView extends ActorMessage {
    private ContentView contentView;
    
    public RecordContentView(ContentView contentView) {
        super(ActorMessageType.RecordContentView);
        this.contentView = contentView;
    }
    
    public ContentView getContentView() {
        return this.contentView;
    }
}
