package peer.data.messages;

import content.view.core.ContentView;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Asks databaser to back up this content view in the history backup
 *
 */
public class BackupContentViewInHistoryRequest extends ActorMessage {
    private ContentView contentView;
    
    public BackupContentViewInHistoryRequest(ContentView contentView) {
        super(ActorMessageType.BackUpContentViewInHistoryRequest);
        this.contentView = contentView;
    }
    
    public ContentView getContentView() {
        return this.contentView;
    }
}
