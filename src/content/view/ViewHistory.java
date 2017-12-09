package content.view;

import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates the view history
 *
 */
public class ViewHistory {
    private List<ContentView> history;
    
    public ViewHistory(List<ContentView> history) {
        this.history = history;
    }
    
    public Iterator<ContentView> iterator() {
        return this.history.iterator();
    }
}
