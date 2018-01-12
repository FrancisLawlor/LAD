package content.view.messages;

import content.view.core.ContentView;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Content View Addition to the ContentViews in the header of a ContentFile
 * Tells Databaser to update the header of the file with new Content View
 *
 */
public class ContentViewAddition extends ActorMessage {
    private ContentView contentView;
    
    public ContentViewAddition(ContentView contentView) {
        super(ActorMessageType.ContentViewAddition);
        this.contentView = contentView;
    }
    
    public ContentView getContentView() {
        return this.contentView;
    }
}
