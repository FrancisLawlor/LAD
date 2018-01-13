package content.recommend.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import content.recommend.heuristic.WeightedProbabilityAggregationHeuristic;
import content.recommend.heuristic.WeightedProbabilityHistoryHeuristic;
import content.recommend.messages.HistoryRecommendationGeneratorInit;
import content.recommend.messages.PeerRecommendation;
import content.recommend.messages.PeerRecommendationAggregatorInit;
import content.recommend.messages.PeerRecommendationRequest;
import content.recommend.messages.RecommendationsForUser;
import content.recommend.messages.RecommendationsForUserRequest;
import peer.frame.actors.PeerToPeerActor;
import peer.frame.core.ActorNames;
import peer.frame.core.ActorPaths;
import peer.frame.core.UniversalId;
import peer.frame.exceptions.UnknownMessageException;
import peer.frame.messages.PeerToPeerActorInit;

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
        final ActorRef aggregator = getContext().actorOf(Props.create(PeerRecommendationAggregator.class), ActorNames.AGGREGATOR);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.AGGREGATOR);
        aggregator.tell(peerIdInit, getSelf());
        PeerRecommendationAggregatorInit init = new PeerRecommendationAggregatorInit(new WeightedProbabilityAggregationHeuristic());
        aggregator.tell(init, getSelf());
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
        UniversalId requestingPeerId = peerRecommendationRequest.getOriginalRequester();
        final ActorRef generator = getContext().actorOf(Props.create(HistoryRecommendationGenerator.class), ActorNames.getHistoryGeneratorName(requestingPeerId));
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.getHistoryGeneratorName(requestingPeerId));
        generator.tell(peerIdInit, getSelf());
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