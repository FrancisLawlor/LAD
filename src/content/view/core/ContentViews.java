package content.view.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import content.frame.core.Content;

/**
 * Content Views header for Content File
 * Lists recent content views containing Peer ID and content info
 * Helpers determine similar peers by similar content views
 *
 */
public class ContentViews implements Iterable<ContentView> {
    private Content content;
    private List<ContentView> recentContentViews;
    
    public ContentViews(Content content) {
        this.content = content;
        this.recentContentViews = new LinkedList<ContentView>();
    }
    
    public Content getContent() {
        return this.content;
    }
    
    public void addContentView(ContentView contentView) {
        this.recentContentViews.add(contentView);
    }
    
    public Iterator<ContentView> iterator() {
        return this.recentContentViews.iterator();
    }
}
