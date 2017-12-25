package peer.data;

import content.core.Content;
import content.core.ContentFileExistenceRequest;
import content.core.ContentFileRequest;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.xcept.UnknownMessageException;

/**
 * Actor that handles the database
 *
 */
public class Databaser extends PeerToPeerActor {
    private Database db;
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            this.initialisePeerToPeerActor(init);
        }
        else if (message instanceof DatabaserInit) {
            DatabaserInit init = (DatabaserInit) message;
            this.initialiseDatabaser(init.getDatabase());
        }
        else if (message instanceof ContentFileExistenceRequest) {
            ContentFileExistenceRequest request = (ContentFileExistenceRequest) message;
            this.processContentFileExistenceRequest(request);
        }
        else if (message instanceof ContentFileRequest) {
            ContentFileRequest request = (ContentFileRequest) message;
            this.processContentFileRequest(request);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Initialise the Database in the databaser actor's state
     * @param db
     */
    protected void initialiseDatabaser(Database db) {
        this.db = db;
    }
    
    /**
     * Checks database to see if there is a file stored matching this Content object's description
     * @param request
     */
    protected void processContentFileExistenceRequest(ContentFileExistenceRequest request) {
        Content content = request.getContent();
        
        // To do
    }
    
    /**
     * Returns a content file to requesting actor if it exists in the database
     * @param request
     */
    protected void processContentFileRequest(ContentFileRequest request) {
        Content content = request.getContent();
        
        // To do
    }
}
