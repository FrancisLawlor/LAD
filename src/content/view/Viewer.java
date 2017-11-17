package content.view;

import akka.actor.UntypedActor;

/**
 * Handles view related matters
 * Sends requests to local Recommender
 * Receives back Recommendations For User
 * When a recommendation is selected it retrieves content with Retrievers
 *
 */
public class Viewer extends UntypedActor {
    
    
    @Override
    public void onReceive(Object message) {
        
    }
}
