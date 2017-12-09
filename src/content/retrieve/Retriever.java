package content.retrieve;

import akka.actor.UntypedActor;
import core.xcept.UnknownMessageException;

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
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Viewer delegates to a local Retriever to request Content
     * @param request
     */
    protected void processLocalRetrieveContentRequest(LocalRetrieveContentRequest request) {
        
    }
    
    /**
     * This peer's retriever is being asked by another peer to find the content
     */
    protected void processPeerRetrieveContentRequest(PeerRetrieveContentRequest request) {
        
    }
    
    /**
     * Retriever will send retrieved content back to viewer
     * @param retrievedContent
     */
    protected void processRetrievedContent(RetrievedContent retrievedContent) {
        
    }
}
