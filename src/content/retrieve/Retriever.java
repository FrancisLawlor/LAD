package content.retrieve;

import akka.actor.UntypedActor;

/**
 * Retrieves Content from network for requester
 *
 */
public class Retriever extends UntypedActor {
    
    
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof LocalRetrieveContentRequest) {
            LocalRetrieveContentRequest retrievedContentRequest = 
                    (LocalRetrieveContentRequest) message;
            this.processLocalRetrieveContentRequest(retrievedContentRequest);
        }
        else if (message instanceof LocalRetrieveContentRequest) {
            PeerRetrieveContentRequest retrievedContentRequest = 
                    (PeerRetrieveContentRequest) message;
            this.processPeerRetrieveContentRequest(retrievedContentRequest);
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
     * Viewer will delegate to a local Retriever and request Content
     * @param request
     */
    protected void processLocalRetrieveContentRequest(
            LocalRetrieveContentRequest request) {
        
    }
    
    /**
     * A Peer's Retriever will ask this peer's Retriever to find the content
     */
    protected void processPeerRetrieveContentRequest(
            PeerRetrieveContentRequest request) {
        
    }
    
    /**
     * Retriever will send retrieved content back to viewer
     * @param retrievedContent
     */
    protected void processRetrievedContent(RetrievedContent retrievedContent) {
        
    }
}
