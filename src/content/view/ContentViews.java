package content.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Content Views header for Content File
 * Lists recent content views containing Peer ID and content info
 * Helpers determine similar peers by similar content views
 *
 */
public class ContentViews implements Iterable<ContentView> {
    private List<ContentView> recentContentViews;
    
    public ContentViews() {
        this.recentContentViews = new LinkedList<ContentView>();
    }
    
    public void addContentView(ContentView contentView) {
        this.recentContentViews.add(contentView);
    }
    
    public Iterator<ContentView> iterator() {
        return this.recentContentViews.iterator();
    }
}
