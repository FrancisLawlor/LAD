package content.retrieve;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import content.core.Content;
import content.core.ContentFile;
import content.core.ContentFileExistenceRequest;
import content.core.ContentFileExistenceResponse;
import content.core.ContentFileRequest;
import content.core.ContentFileResponse;
import content.core.GossipContentRequest;
import content.core.GossipContentResponse;
import filemanagement.fileretrieval.FileManager;
import filemanagement.filewrapper.FileUnwrapper;
import peer.core.ActorNames;
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
    private static final String TRANSFER_PORT = "10002";
    
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
        else if (message instanceof PeerRetrieveContentRequest) {
            PeerRetrieveContentRequest retrievedContentRequest = (PeerRetrieveContentRequest) message;
            this.processPeerRetrieveContentRequest(retrievedContentRequest);
        }
        else if (message instanceof ContentFileExistenceResponse) {
            ContentFileExistenceResponse response = (ContentFileExistenceResponse) message;
            this.processContentFileExistenceResponse(response);
        }
        else if (message instanceof ContentFileResponse) {
            ContentFileResponse response = (ContentFileResponse) message;
            this.processContentFileResponse(response);
        }
        else if (message instanceof GossipContentResponse) {
            GossipContentResponse response = (GossipContentResponse) message;
            this.processGossipContentResponse(response);
        }
        else if (message instanceof RetrievedContent) {
            RetrievedContent retrievedContent = (RetrievedContent) message;
            this.processRetrievedContent(retrievedContent);
        }
        else if (message instanceof RetrievedContentFile) {
            RetrievedContentFile retrievedContentFile = (RetrievedContentFile) message;
            this.processRetrievedContentFile(retrievedContentFile);
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
        Content content = response.getContent();
        if (response.hasContentFile()) {
            ContentFileRequest request = new ContentFileRequest(content);
            ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
            databaser.tell(request, getSelf());
        }
        else {
            GossipContentRequest request = new GossipContentRequest(content);
            ActorSelection gossiper = getContext().actorSelection(ActorPaths.getPathToGossiper());
            gossiper.tell(request, getSelf());
        }
    }
    
    /**
     * Send back the content file that has been retrieved locally from this peer
     * @param response
     */
    protected void processContentFileResponse(ContentFileResponse response) {
        ContentFile contentFile = response.getContentFile();
        Content content = contentFile.getContent();
        UniversalId requesterId = this.contentRequestedBy.remove(content);
        TransferInfo transferInfo = new TransferInfo(super.peerId, TRANSFER_PORT);
        
        final ActorRef transferer = getContext().actorOf(Props.create(Transferer.class), ActorNames.TRANSFERER);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.TRANSFERER);
        transferer.tell(peerIdInit, getSelf());
        TransfererInit init = new TransfererInit(transferInfo);
        transferer.tell(init, getSelf());
        TransferSendStart sendStart = new TransferSendStart(response.getContentFile());
        transferer.tell(sendStart, getSelf());
        
        RetrievedContent retrievedContent = new RetrievedContent(requesterId, super.peerId, content, transferInfo);
        ActorSelection outboundComm = getContext().actorSelection(ActorPaths.getPathToOutComm());
        outboundComm.tell(retrievedContent, getSelf());
    }
    
    /**
     * Ask another peer to fulfil the retrieve content request this peer couldn't fulfil
     * Other peer is suspected by the gossiper of having the content file
     * @param response
     */
    protected void processGossipContentResponse(GossipContentResponse response) {
        Content content = response.getContent();
        UniversalId requesterId = this.contentRequestedBy.remove(content);
        UniversalId newTargetPeerId = response.getPeerId();
        PeerRetrieveContentRequest request = new PeerRetrieveContentRequest(requesterId, newTargetPeerId, content);
        ActorSelection outboundComm = getContext().actorSelection(ActorPaths.getPathToOutComm());
        outboundComm.tell(request, getSelf());
    }
    
    /**
     * Retriever will send retrieved content back to viewer
     * @param retrievedContent
     */
    protected void processRetrievedContent(RetrievedContent retrievedContent) {
        final ActorRef transferer = getContext().actorOf(Props.create(Transferer.class), ActorNames.TRANSFERER);
        PeerToPeerActorInit peerIdInit = new PeerToPeerActorInit(super.peerId, ActorNames.TRANSFERER);
        transferer.tell(peerIdInit, getSelf());
        TransfererInit init = new TransfererInit(retrievedContent.getTransferInfo());
        transferer.tell(init, getSelf());
        TransferReceiveStart receiveStart = new TransferReceiveStart(retrievedContent);
        transferer.tell(receiveStart, getSelf());
    }
    
    /**
     * A content file retrieved from the Transferer child actor will be passed to the Databaser for local peer storage
     * The media segment of this contentFile will be written to a temporary directory for viewing by the GUI
     * The RetrievedContent message will be passed back to the viewer to signal the media file is available in the temporary directory to view
     * 
     * @param retrievedContentFile
     */
    protected void processRetrievedContentFile(RetrievedContentFile retrievedContentFile) throws IOException {
        ContentFile contentFile = retrievedContentFile.getContentFile();
        this.saveMediaFileForView(contentFile);
        
        RetrievedContent retrievedContent = retrievedContentFile.getRetrievedContent();
        ActorSelection viewer = getContext().actorSelection(ActorPaths.getPathToViewer());
        viewer.tell(retrievedContent, getSelf());
        
        ActorSelection databaser = getContext().actorSelection(ActorPaths.getPathToDatabaser());
        databaser.tell(retrievedContentFile, getSelf());
    }
    
    /**
     * Save Media segment of ContentFile bytes to temporary viewing directory
     * @param contentFile
     * @throws IOException
     */
    private void saveMediaFileForView(ContentFile contentFile) throws IOException {
        String fileName = contentFile.getContent().getFileName();
        String fileFormat = contentFile.getContent().getFileFormat();
        byte[] media = FileUnwrapper.extractFileArray(contentFile.getBytes());
        FileManager.writeMediaFile(fileName, fileFormat, media);
    }
}
