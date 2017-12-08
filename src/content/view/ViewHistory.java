package content.view;

import java.util.Iterator;
import java.util.List;

/**
 * Encapsulates the view history
 *
 */
public class ViewHistory {
    private List<View> history;
    
    public ViewHistory(List<View> history) {
        this.history = history;
    }
    
    public Iterator<View> iterator() {
        return this.history.iterator();
    }
}
