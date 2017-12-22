package content.recommend;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import content.recommend.heuristic.DeterministicAggregationHeuristic;
import content.recommend.heuristic.DeterministicHistoryHeuristic;
import content.recommend.heuristic.WeightedProbabilityAggregationHeuristic;
import content.recommend.heuristic.WeightedProbabilityHistoryHeuristic;
import core.ActorNames;
import core.ActorPaths;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import core.xcept.UnknownMessageException;

/**
 * Receives requests for Recommendations For User from the viewer
 * Delegates to a Peer Recommendation Aggregator that aggregates peer recommendations
 * Also exists on the other side of peer to peer communication
 * Receives requests for Peer Recommendations from a peer
 * Delegates to a History Recommendation Generator that generates a recommendation based on this peer's history
 * Receives back a Peer Recommendation to be sent back to the original requester as this peer's recommendation
 *
 */
public class Recommender extends PeerToPeerActor {
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof RecommendationsForUserRequest) {
            RecommendationsForUserRequest recommendationForUserRequest = 
        	    (RecommendationsForUserRequest) message;
            this.processRecommendationForUserRequest(recommendationForUserRequest);
        }
        else if (message instanceof RecommendationsForUser) {
            RecommendationsForUser recommendations = 
                    (RecommendationsForUser) message;
            this.processRecommendationsForUser(recommendations);
        }
        else if (message instanceof PeerRecommendationRequest) {
            PeerRecommendationRequest peerRecommendationRequest = 
                    (PeerRecommendationRequest) message;
            this.processPeerRecommendationRequest(peerRecommendationRequest);
        }
        else if (message instanceof PeerRecommendation) {
            PeerRecommendation peerRecommendation = 
                    (PeerRecommendation) message;
            this.processPeerRecommendation(peerRecommendation);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Creates an aggregator that will aggregate recommendations from peers
     * @param request
     */
    protected void processRecommendationForUserRequest(RecommendationsForUserRequest request) {
<<<<<<< HEAD
        final ActorRef aggregator = 
                getContext().actorOf(Props.create(PeerRecommendationAggregator.class), ActorNames.AGGREGATOR);
        PeerRecommendationAggregatorInit init = 
                new PeerRecommendationAggregatorInit(new DeterministicAggregationHeuristic());
        aggregator.tell(init, getSelf());
=======
        final ActorRef aggregator = getContext().actorOf(Props.create(PeerRecommendationAggregator.class), ActorNames.AGGREGATOR);
        
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.AGGREGATOR);
        aggregator.tell(peerIdInit, getSelf());

        PeerRecommendationAggregatorInit init = new PeerRecommendationAggregatorInit(new WeightedProbabilityAggregationHeuristic());
        aggregator.tell(init, getSelf());
        
>>>>>>> 23538a8cfa942af2d6546ab22412db5a79abe27d
        aggregator.tell(request, getSelf());
    }
    
    /**
     * Sends recommendations for user back to viewer
     * @param recommendations
     */
    protected void processRecommendationsForUser(RecommendationsForUser recommendations) {
        ActorSelection viewer = getContext().actorSelection(ActorPaths.getPathToViewer());
        viewer.tell(recommendations, getSelf());
    }
    
    /**
     * Message asks for this Peer's recommendation
     * This peer will request its own View History to determine what recommendation to send back
     * @param recommendation
     */
    protected void processPeerRecommendationRequest(PeerRecommendationRequest peerRecommendationRequest) {
        final ActorRef generator = getContext().actorOf(Props.create(HistoryRecommendationGenerator.class), ActorNames.HISTORY_GENERATOR);
        
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.HISTORY_GENERATOR);
        generator.tell(peerIdInit, getSelf());
        
        UniversalId requestingPeerId = peerRecommendationRequest.getOriginalRequester();
        HistoryRecommendationGeneratorInit init = new HistoryRecommendationGeneratorInit(requestingPeerId, new WeightedProbabilityHistoryHeuristic());
        generator.tell(init, getSelf());
        
        generator.tell(peerRecommendationRequest, getSelf());
    }
    
    /**
     * Sends Peer Recommendation, generated by this peer, back to its original requester
     * @param peerRecommendation
     */
    protected void processPeerRecommendation(PeerRecommendation peerRecommendation) {        
        ActorSelection communicator = getContext().actorSelection(ActorPaths.getPathToOutComm());
        communicator.tell(peerRecommendation, getSelf());
    }
}
