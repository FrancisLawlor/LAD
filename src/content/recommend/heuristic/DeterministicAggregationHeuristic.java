package content.recommend.heuristic;

import java.util.LinkedList;
import java.util.List;

import content.core.Content;
import content.recommend.PeerRecommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.WeightedPeerRecommendation;

/**
 * Deterministic Heuristic to be used for testing
 * Returns aggregated content deterministically from weighted peer recommendations
 *
 */
public class DeterministicAggregationHeuristic implements AggregationHeuristic {
    /**
     * Sorts Peer Recommendations by Weight
     * Adds Content from Peer Recommendations to Content List deterministically
     * Does this by cycling through the ranked content for each peer recommendation successively
     * How: Pays attention to the first content recommendation from each peer first,
     * Then pays attention to the second from the top for each peer,
     * Then pays attention to the third from the top for each peer, etc.
     */
    public RecommendationsForUser getRecommendationsForUser(List<WeightedPeerRecommendation> peerRecommends) {
        peerRecommends.sort(new WeightedPeerRecommendationComparator());
        
        List<Content> contentList = new LinkedList<Content>();
        for (int rank = 0; rank < HistoryHeuristic.TOP_N && contentList.size() <= LIMIT; rank++){
            for (int i = 0; i < peerRecommends.size() && contentList.size() <= LIMIT; i++) {
                PeerRecommendation recommendation = peerRecommends.get(i).getPeerRecommendation();
                if (rank < recommendation.size()) {
                    Content content = recommendation.getContentAtRank(rank);
                    contentList.add(content);
                }
            }
        }
        
        RecommendationsForUser recommendationsForUser = new RecommendationsForUser(contentList);
        return recommendationsForUser;
    }
}
