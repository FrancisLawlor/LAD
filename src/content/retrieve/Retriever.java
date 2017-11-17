package content.retrieve;

import akka.actor.UntypedActor;

/**
 * 
 *
 */
public class Retriever extends UntypedActor {
    
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof RetrieveContentRequest) {
            RetrieveContentRequest retrievedContentRequest = 
                    (RetrieveContentRequest) message;
            this.processRetrieveContentRequest(retrievedContentRequest);
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent retrievedContent = 
                    (RetrievedContent) message;
            this.processRetrievedContent(retrievedContent);
        }
        else {
            throw new RuntimeException("Unrecognised Message; Debug");
        }
    }
    
    /**
     * 
     * @param request
     */
    protected void processRetrieveContentRequest(RetrieveContentRequest request) {
        
    }
    
    /**
     * 
     * @param retrievedContent
     */
    protected void processRetrievedContent(RetrievedContent retrievedContent) {
        
    }
}
