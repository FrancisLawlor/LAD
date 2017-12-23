package content.view;

import java.util.concurrent.BlockingQueue;

import akka.actor.ActorSelection;
import content.recommend.RecommendationsForUser;
import content.recommend.RecommendationsForUserRequest;
import content.retrieve.LocalRetrieveContentRequest;
import content.retrieve.RetrievedContent;
import core.ActorPaths;
import core.PeerToPeerActor;
import core.PeerToPeerActorInit;
import core.xcept.UnknownMessageException;

/**
 * Handles view related matters
 * Sends requests to local Recommender
 * Receives back Recommendations For User
 * When a recommendation is selected it retrieves content with Retrievers
 *
 */
public class Viewer extends PeerToPeerActor {
    private BlockingQueue<RecommendationsForUser> recommendationsQueue;
    
    /**
     * Actor Message Processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof ViewerInit) {
            ViewerInit init = (ViewerInit) message;
            this.recommendationsQueue = init.getRecommendationsQueue();
        }
        else if (message instanceof RecommendationsForUserRequest) {
            RecommendationsForUserRequest request = (RecommendationsForUserRequest) message;
            this.processRecommendationsForUserRequest(request);
        }
        else if (message instanceof LocalRetrieveContentRequest) {
            LocalRetrieveContentRequest request = (LocalRetrieveContentRequest) message;
            this.processLocalRetrieveContentRequest(request);
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
     * Viewer will ask the Recommender Actor for Recommendations For User
     */
    public void processRecommendationsForUserRequest(RecommendationsForUserRequest request) {
        ActorSelection recommender = getContext().actorSelection(ActorPaths.getPathToRecommender());
        recommender.tell(request, getSelf());
    }
    
    /**
     * Viewer will ask the Retriever Actor to retrieve Content for viewing
     * @param content
     * @param fromPeerId
     */
    public void processLocalRetrieveContentRequest(LocalRetrieveContentRequest request) {
        ActorSelection retriever = getContext().actorSelection(ActorPaths.getPathToRetriever());
        retriever.tell(request, getSelf());
    }
    
    /**
     * Send Recommendation for User to State Machine Controller
     * @param recommendations
     */
    protected void processRecommendationsForUser(RecommendationsForUser recommendations) {
        this.recommendationsQueue.add(recommendations);
    }
    
    /**
     * Send Retrieved Content for User to State Machine Controller
     * @param content
     */
    protected void processRetrievedContent(RetrievedContent content) {
        
    }
    
}
