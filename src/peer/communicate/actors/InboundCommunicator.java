package peer.communicate.actors;

import akka.actor.ActorSelection;
import content.recommend.messages.PeerRecommendation;
import content.recommend.messages.PeerRecommendationRequest;
import content.retrieve.messages.PeerRetrieveContentRequest;
import content.retrieve.messages.RetrievedContent;
import peer.frame.actors.PeerToPeerActor;
import peer.frame.core.ActorPaths;
import peer.frame.exceptions.PeerToPeerRequestOriginPeerIdMismatchException;
import peer.frame.exceptions.PeerToPeerRequestTargetPeerIdMismatchException;
import peer.frame.exceptions.UnknownMessageException;
import peer.frame.messages.PeerToPeerActorInit;
import peer.graph.messages.PeerWeightUpdateRequest;
import peer.graph.messages.RemotePeerWeightedLinkAddition;

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
        else if (message instanceof RemotePeerWeightedLinkAddition) {
            RemotePeerWeightedLinkAddition addition = (RemotePeerWeightedLinkAddition) message;
            this.processRemotePeerWeightedLinkAddition(addition);
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
            throw new PeerToPeerRequestTargetPeerIdMismatchException(request.getOriginalTarget(), super.peerId);
        
        ActorSelection recommender = getContext().actorSelection(ActorPaths.getPathToRecommender());
        recommender.tell(request, getSelf());
    }
    
    /**
     * Send this recommendation received from a peer back to this peer's PeerRecommendationAggregator
     * @param recommendation
     */
    protected void processPeerRecommendation(PeerRecommendation recommendation) {
        if (!recommendation.getOriginalRequester().equals(super.peerId)) 
            throw new PeerToPeerRequestOriginPeerIdMismatchException(recommendation.getOriginalRequester(), super.peerId);
        
        ActorSelection aggregator = getContext().actorSelection(ActorPaths.getPathToAggregator());
        aggregator.tell(recommendation, getSelf());
    }
    
    /**
     * Send this content retrieval request from another peer to this peer's Retriever actor to retrieve content for the requesting peer
     * @param contentRequest
     */
    protected void processPeerRetrieveContentRequest(PeerRetrieveContentRequest contentRequest) {
        if (!contentRequest.getOriginalTarget().equals(super.peerId)) 
            throw new PeerToPeerRequestTargetPeerIdMismatchException(contentRequest.getOriginalTarget(), super.peerId);
        
        ActorSelection retriever = getContext().actorSelection(ActorPaths.getPathToRetriever());
        retriever.tell(contentRequest, getSelf());
    }
    
    /**
     * Send the content retrieved from another peer back to this peer's retriever
     * @param content
     */
    protected void processRetrievedContent(RetrievedContent retrievedContent) {
        if (!retrievedContent.getOriginalRequester().equals(super.peerId)) 
            throw new PeerToPeerRequestOriginPeerIdMismatchException(retrievedContent.getOriginalRequester(), super.peerId);
        
        ActorSelection aggregator = getContext().actorSelection(ActorPaths.getPathToRetriever());
        aggregator.tell(retrievedContent, getSelf());
    }
    
    /**
     * Will tell the PeerWeightedLinkor to update weighted linked between this peer and the requesting peer if a link is recorded
     * @param request
     */
    protected void processPeerWeightUpdateRequest(PeerWeightUpdateRequest updateWeightRequest) {
        if (!updateWeightRequest.getOriginalTarget().equals(super.peerId)) 
            throw new PeerToPeerRequestTargetPeerIdMismatchException(updateWeightRequest.getOriginalTarget(), super.peerId);
        
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerWeightedLinkor.tell(updateWeightRequest, getSelf());
    }
    
    /**
     * Will tell the PeerWeightedLinkorDHM to add a weighted link between this peer and the requesting peer
     * @param addition
     */
    protected void processRemotePeerWeightedLinkAddition(RemotePeerWeightedLinkAddition addition) {
        if (!addition.getOriginalTarget().equals(super.peerId)) 
            throw new PeerToPeerRequestTargetPeerIdMismatchException(addition.getOriginalTarget(), super.peerId);
        
        ActorSelection peerWeightedLinkor = getContext().actorSelection(ActorPaths.getPathToPeerLinker());
        peerWeightedLinkor.tell(addition, getSelf());
    }
}
