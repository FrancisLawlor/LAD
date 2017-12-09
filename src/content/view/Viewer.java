package content.view;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import content.recommend.Recommendation;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import content.retrieve.LocalRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import core.StateMachine;

/**
 * Handles view related matters
 * Sends requests to local Recommender
 * Receives back Recommendations For User
 * When a recommendation is selected it retrieves content with Retrievers
 *
 */
public class Viewer extends UntypedActor {
    private StateMachine stateMachine;
    
    public Viewer(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }
    
    public void getRecommendationsForUser() {
        ActorSelection recommender = getContext().actorSelection("user/recommender");
        recommender.tell(new RecommendationsForUserRequest(), getSelf());
    }
    
    public void getContent(Recommendation recommendation) {
        ActorSelection retriever = getContext().actorSelection("user/retriever");
        retriever.tell(new LocalRetrieveContentRequest(recommendation), getSelf());
    }
    
    @Override
    public void onReceive(Object message) {
        if (message instanceof RecommendationsForUser) {
            RecommendationsForUser recommendations = (RecommendationsForUser) message;
            this.processRecommendationsForUser(recommendations);
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent content = (RetrievedContent) message;
            this.processRetrievedContent(content);
        }
        else {
            throw new RuntimeException("Message unrecognised; Debug!");
        }
    }
    
    protected void processRecommendationsForUser(RecommendationsForUser recommendations) {
        this.stateMachine.setRecommendationsForUser(recommendations.iterator());
    }
    
    protected void processRetrievedContent(RetrievedContent content) {
        this.stateMachine.setRetrievedContentUrlString(content.getURLString());
    }
    
}
