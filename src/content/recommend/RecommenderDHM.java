package content.recommend;

import akka.actor.ActorRef;
import akka.actor.Props;
import content.recommend.heuristic.WeightedProbabilityAggregationHeuristic;
import peer.core.ActorNames;
import peer.core.PeerToPeerActorInit;

/**
 * Recommender variant that uses a PeerRecommendationAggregator that uses the PeerWeightedLinkorDHM
 *
 */
public class RecommenderDHM extends Recommender {
    /**
     * Creates an aggregator that will aggregate recommendations from peers
     * PeerRecommendationAggregator will use implementation that uses PeerWeightedLinkorDHM
     * @param request
     */
    @Override
    protected void processRecommendationForUserRequest(RecommendationsForUserRequest request) {
        final ActorRef aggregator = getContext().actorOf(Props.create(PeerRecommendationAggregatorDHM.class), ActorNames.AGGREGATOR);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.AGGREGATOR);
        aggregator.tell(peerIdInit, getSelf());
        PeerRecommendationAggregatorInit init = new PeerRecommendationAggregatorInit(new WeightedProbabilityAggregationHeuristic());
        aggregator.tell(init, getSelf());
        aggregator.tell(request, getSelf());
    }
}
