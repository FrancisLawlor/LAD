package content.retrieve;

import akka.actor.ActorSelection;
import peer.core.ActorPaths;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.xcept.UnknownMessageException;

/**
 * Retrieves Content from network for requester
 *
 */
public class Retriever extends PeerToPeerActor {
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
     */
    protected void processPeerRetrieveContentRequest(PeerRetrieveContentRequest request) {
        // Find content locally
        // If it has been deleted locally then...
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
