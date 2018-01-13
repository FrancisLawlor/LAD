package peer.data.messages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import content.frame.core.Content;
import peer.frame.core.ActorMessageType;
import peer.frame.messages.ActorMessage;

/**
 * Returns an iterable list of saved content local to this peer
 *
 */
public class LocalSavedContentResponse extends ActorMessage implements Iterable<Content> {
    private List<Content> savedContentManifest;
    
    public LocalSavedContentResponse() {
        super(ActorMessageType.LocalSavedContentResponse);
        this.savedContentManifest = new LinkedList<Content>();
    }
    
    public void add(Content content) {
        this.savedContentManifest.add(content);
    }
    
    public Iterator<Content> iterator() {
        return this.savedContentManifest.iterator();
    }
}
