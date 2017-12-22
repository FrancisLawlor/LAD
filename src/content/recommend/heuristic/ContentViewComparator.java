package content.recommend.heuristic;

import java.util.Comparator;

import content.view.ContentView;

public class ContentViewComparator implements Comparator<ContentView> {
    public int compare(ContentView a, ContentView b) {
        int comparison;
        if (a.getScore() < b.getScore()) {
            comparison = 1;
        }
        else if (a.getScore() > b.getScore()) {
            comparison = -1;
        }
        else {
            comparison = 0;
        }
        return comparison;
    }
}
