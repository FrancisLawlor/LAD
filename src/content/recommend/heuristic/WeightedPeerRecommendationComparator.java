package content.recommend.heuristic;

import java.util.Comparator;

import content.recommend.WeightedPeerRecommendation;

public class WeightedPeerRecommendationComparator implements Comparator<WeightedPeerRecommendation> {
    
    public int compare(WeightedPeerRecommendation a, WeightedPeerRecommendation b) {
        int comparison;
        if (a.getWeight() < b.getWeight()) {
            comparison = -1;
        }
        else if (a.getWeight() > b.getWeight()) {
            comparison = 1;
        }
        else {
            comparison = 0;
        }
        return comparison;
    }
}
