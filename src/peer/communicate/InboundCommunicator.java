package peer.communicate;

import akka.actor.ActorSelection;
import content.recommend.PeerRecommendation;
import content.recommend.PeerRecommendationRequest;
import content.retrieve.PeerRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import core.ActorPaths;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.UniversalId;
import core.xcept.RequestCommunicationOriginPeerIdMismatchException;
import core.xcept.RequestCommunicationTargetPeerIdMismatchException;
import core.xcept.UnknownMessageException;
import peer.graph.weight.PeerWeightUpdateRequest;

/**
 * Communicator actor that receives inbound communication to this peer from another peer
 * Receives Actor Messages to be relayed back to Actors in this peer's internal System
 * These Actor Messages come from actors that double as Apache Camel Processors
 * These Apache Camel Processors process JSON REST message Exchanges captured by the DistributedRecommenderRouter
 * They convert the JSON REST messages to Actor Messages and send here to the InboundCommunicator for relaying to internal actors
 *
 */
public class InboundCommunicator extends PeerToPeerActor {
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof PeerRecommendationRequest) {
            PeerRecommendationRequest request = (PeerRecommendationRequest) message;
            this.processPeerRecommendationRequest(request);
        }
        else if (message instanceof PeerRecommendation) {
            PeerRecommendation recommendation = (PeerRecommendation) message;
            this.processPeerRecommendation(recommendation);
        }
        else if (message instanceof PeerRetrieveContentRequest) {
            PeerRetrieveContentRequest contentRequest = (PeerRetrieveContentRequest) message;
            this.processPeerRetrieveContentRequest(contentRequest);
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent content = (RetrievedContent) message;
            this.processRetrievedContent(content);
        }
        else if (message instanceof PeerWeightUpdateRequest) {
            PeerWeightUpdateRequest request = (PeerWeightUpdateRequest) message;
            this.processPeerWeightUpdateRequest(request);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Forwards on a request from another peer for recommendations to this peer's Recommender Actor
     * @param request
     */
    protected void processPeerRecommendationRequest(PeerRecommendationRequest request) {
        if (!request.getOriginalTarget().equals(super.peerId))
            throw new RequestCommunicationTargetPeerIdMismatchException();
        
        ActorSelection recommender = getContext().actorSelection(ActorPaths.getPathToRecommender());
        recommender.tell(request, getSelf());
    }
    
    /**
     * Send this recommendation received from a peer back to this peer's PeerRecommendationAggregator
     * @param recommendation
     */
    protected void processPeerRecommendation(PeerRecommendation recommendation) {
        if (!recommendation.getOriginalRequester().equals(super.peerId)) 
            throw new RequestCommunicationOriginPeerIdMismatchException();
        
        ActorSelection aggregator = getContext().actorSelection(ActorPaths.getPathToAggregator());
        aggregator.tell(recommendation, getSelf());
    }
    
    /**
     * Send this content retrieval request from another peer to this peer's Retriever actor to retrieve content for the requesting peer
     * @param contentRequest
     */
    protected void processPeerRetrieveContentRequest(PeerRetrieveContentRequest contentRequest) {
        if (contentRequest.getOriginalTarget().equals(super.peerId)) 
            throw new RequestCommunicationTargetPeerIdMismatchException();
        
        ActorSelection retriever = getContext().actorSelection(ActorPaths.getPathToRetriever());
        retriever.tell(contentRequest, getSelf());
    }
    
    /**
     * Send the content retrieved from another peer back to this peer's retriever
     * @param content
     */
    protected void processRetrievedContent(RetrievedContent retrievedContent) {
        if (!retrievedContent.getOriginalRequester().equals(super.peerId)) 
            throw new RequestCommunicationOriginPeerIdMismatchException();
        
        ActorSelection aggregator = getContext().actorSelection(ActorPaths.getPathToRetriever());
        aggregator.tell(retrievedContent, getSelf());
    }
    
    /**
     * Will update weighted linked between this peer and the requesting peer if a link is recorded
     * @param request
     */
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest updateWeightRequest) {
        if (updateWeightRequest.getOriginalTarget().equals(super.peerId)) 
            throw new RequestCommunicationTargetPeerIdMismatchException();
        
        UniversalId linkedPeerId = updateWeightRequest.getOriginalRequester();
        ActorSelection weighter = getContext().actorSelection(ActorPaths.getPathToWeighter(linkedPeerId));
        weighter.tell(updateWeightRequest, getSelf());
    }
    
    // TEAM NOTE:
    // Consider checking if the original target is in a list of targets for PeerRecommendation and RetrievedContent...
    // ... to stop malicious injection of fake recommendations and retrieved content!
    // We could have a list of RequestCommunications that have been sent out
    // Will need to be properly designed because Outbound Communicator is a separate actor from Inbound at the moment...
}
