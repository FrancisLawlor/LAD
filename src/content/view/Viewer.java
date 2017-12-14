package content.view;

import akka.actor.ActorSelection;
import content.content.Content;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import content.retrieve.LocalRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import core.ActorNames;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.StateMachine;
import core.UniversalId;
import core.xcept.UnknownMessageException;

/**
 * Handles view related matters
 * Sends requests to local Recommender
 * Receives back Recommendations For User
 * When a recommendation is selected it retrieves content with Retrievers
 *
 */
public class Viewer extends PeerToPeerActor {
    private StateMachine stateMachine;
    
    // Get statemachine somehow
    
    /**
     * Viewer will ask the Recommender Actor for Recommendations For User
     */
    public void getRecommendationsForUser() {
        ActorSelection recommender = getContext().actorSelection("user/" + ActorNames.RECOMMENDER);
        recommender.tell(new RecommendationsForUserRequest(super.peerId), getSelf());
    }
    
    /**
     * Viewer will ask the Retriever Actor to retrieve Content for viewing
     * @param content
     * @param fromPeerId
     */
    public void getContent(Content content, UniversalId fromPeerId) {
        LocalRetrieveContentRequest request = new LocalRetrieveContentRequest(super.peerId, fromPeerId, content);
        ActorSelection retriever = getContext().actorSelection("user/" + ActorNames.RETRIEVER);
        retriever.tell(request, getSelf());
    }
    
    /**
     * Actor Message Processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof RecommendationsForUser) {
            RecommendationsForUser recommendations = (RecommendationsForUser) message;
            this.processRecommendationsForUser(recommendations);
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent content = (RetrievedContent) message;
            this.processRetrievedContent(content);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Send Recommendation for User to State Machine Controller
     * @param recommendations
     */
    protected void processRecommendationsForUser(RecommendationsForUser recommendations) {
        this.stateMachine.setRecommendationsForUser(recommendations.iterator());
    }
    
    /**
     * Send Retrieved Content for User to State Machine Controller
     * @param content
     */
    protected void processRetrievedContent(RetrievedContent content) {
        this.stateMachine.setRetrievedContent(content.getContent());
    }
    
}
