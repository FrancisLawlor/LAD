package content.recommend;

import akka.actor.ActorSelection;
import peer.core.ActorPaths;
import peer.core.UniversalId;
import peer.graph.weight.WeightRequest;

/**
 * Modifies original PeerRecommendationAggregator to get weight from PeerWeightedLinkorDHM
 *
 */
public class PeerRecommendationAggregatorDHM extends PeerRecommendationAggregator {
    /**
     * Asks Weighter what Weight this Peer's Link should have in aggregate Recommendations
     */
    protected void sendPeerLinkWeightRequest(UniversalId peerId) {
        WeightRequest request = new WeightRequest(peerId);
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerWeightedLinkor.tell(request, getSelf());
    }
}
