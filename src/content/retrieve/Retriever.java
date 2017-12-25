package content.retrieve;

import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorSelection;
import content.core.Content;
import content.core.ContentFileExistenceRequest;
import content.core.ContentFileExistenceResponse;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.UniversalId;
import peer.core.xcept.UnknownMessageException;

/**
 * Retrieves Content from network for requester
 *
 */
public class Retriever extends PeerToPeerActor {
    private Map<Content, UniversalId> contentRequestedBy;
    
    public Retriever() {
        this.contentRequestedBy = new HashMap<Content, UniversalId>();
    }
    
    /**
     * Actor Message processing
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            super.initialisePeerToPeerActor(init);
        }
        else if (message instanceof LocalRetrieveContentRequest) {
            LocalRetrieveContentRequest retrievedContentRequest = (LocalRetrieveContentRequest) message;
            this.processLocalRetrieveContentRequest(retrievedContentRequest);
        }
        else if (message instanceof LocalRetrieveContentRequest) {
            PeerRetrieveContentRequest retrievedContentRequest = (PeerRetrieveContentRequest) message;
            this.processPeerRetrieveContentRequest(retrievedContentRequest);
        }
        else if (message instanceof ContentFileExistenceResponse) {
            
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent retrievedContent = (RetrievedContent) message;
            this.processRetrievedContent(retrievedContent);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Viewer is delegating to its local Retriever to request Content from another peer
     * This local retriever will use its communicator to request content from this other peer
     * @param request
     */
    protected void processLocalRetrieveContentRequest(LocalRetrieveContentRequest request) {
        PeerRetrieveContentRequest retrieveRequest = new PeerRetrieveContentRequest(request);
        
        ActorSelection communicator = getContext().actorSelection(ActorPaths.getPathToOutComm());
        communicator.tell(retrieveRequest, getSelf());
    }
    
    /**
     * This peer's retriever is being asked by another peer to find the content
     * It firsts checks if it has it locally stored
     */
    protected void processPeerRetrieveContentRequest(PeerRetrieveContentRequest request) {
        this.contentRequestedBy.put(request.getContent(), request.getOriginalRequester());
        this.hasContentFile(request.getContent());
    }
    
    /**
     * Private helper to check if File is stored on this local peer
     * @return
     */
    private void hasContentFile(Content content) {
        ContentFileExistenceRequest request = new ContentFileExistenceRequest(content);
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        databaser.tell(request, getSelf());
    }
    
    /**
     * An affirmative response will cause the retriever to delegate to the transferer for the transfer
     * A negative response will have the retriever ask the gossiper if it knows who might have the file
     * @param response
     */
    protected void processContentFileExistenceResponse(ContentFileExistenceResponse response) {
        if (response.hasContentFile()) {
            // Get Content File from Databaser
            // Begin transfer with child Transferer
        }
        else {
            // Ask Gossiper if it knows who else might have it
        }
    }
    
    /**
     * Retriever will send retrieved content back to viewer
     * @param retrievedContent
     */
    protected void processRetrievedContent(RetrievedContent retrievedContent) {
        ActorSelection viewer = getContext().actorSelection(ActorPaths.getPathToViewer());
        viewer.tell(retrievedContent, getSelf());
    }
}
