package content.view;

import peer.core.ActorMessage;
import peer.core.ActorMessageType;

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
