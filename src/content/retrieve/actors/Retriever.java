package content.retrieve.actors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import content.frame.core.Content;
import content.frame.core.ContentFile;
import content.frame.messages.ContentFileExistenceRequest;
import content.frame.messages.ContentFileExistenceResponse;
import content.frame.messages.ContentFileRequest;
import content.frame.messages.ContentFileResponse;
import content.retrieve.core.TransferInfo;
import content.retrieve.messages.LocalRetrieveContentRequest;
import content.retrieve.messages.PeerRetrieveContentRequest;
import content.retrieve.messages.RetrievedContent;
import content.retrieve.messages.RetrievedContentFile;
import content.retrieve.messages.TransferReceiveStart;
import content.retrieve.messages.TransferSendStart;
import content.retrieve.messages.TransfererInit;
import content.similarity.messages.SimilarContentViewPeerAlert;
import content.similarity.messages.SimilarContentViewPeerRequest;
import content.similarity.messages.SimilarContentViewPeerResponse;
import content.view.core.ContentView;
import content.view.core.ContentViews;
import filemanagement.fileretrieval.MediaFileSaver;
import filemanagement.filewrapper.FileUnwrapper;
import peer.frame.actors.PeerToPeerActor;
import peer.frame.core.ActorNames;
import peer.frame.core.ActorPaths;
import peer.frame.core.UniversalId;
import peer.frame.core.UniversalIdResolver;
import peer.frame.exceptions.UnknownMessageException;
import peer.frame.messages.PeerToPeerActorInit;

/**
 * Retrieves Content from network for requester
 *
 */
public class Retriever extends PeerToPeerActor {
    private static String TRANSFER_PORT = "10003";
    
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
            TRANSFER_PORT = "" + (Integer.parseInt(UniversalIdResolver.getIdPort(super.peerId)) + 1);
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
        else if (message instanceof SimilarContentViewPeerResponse) {
            SimilarContentViewPeerResponse response = (SimilarContentViewPeerResponse) message;
            this.processSimilarContentViewPeerResponse(response);
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
     * A negative response will have the retriever ask the Similaritor if it knows who might have the file
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
            SimilarContentViewPeerRequest request = new SimilarContentViewPeerRequest(content);
            ActorSelection similaritor = getContext().actorSelection(ActorPaths.getPathToSimilaritor());
            similaritor.tell(request, getSelf());
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
     * Other peer is suspected by the similaritor of having the content from previous similar content views
     * @param response
     */
    protected void processSimilarContentViewPeerResponse(SimilarContentViewPeerResponse response) {
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
        this.sendPeerSimilarContentViews(contentFile);
        
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
        MediaFileSaver.writeMediaFile(fileName, fileFormat, media);
    }
    
    /**
     * Send Content Views from Content File that will help Similaritor determine similar peers
     * Similaritor will reweight the similarity of peers in the weighted links of the peer graph to influence future recommendations
     * Similaritor also remembers what peers have viewed similar content...
     * ...this helps Retriever in the future retrieve content from similar peers if this peer has deleted it
     * @param contentFile
     */
    private void sendPeerSimilarContentViews(ContentFile contentFile) {
        byte[] headerArray = FileUnwrapper.extractHeaderArray(contentFile.getBytes());
        String json = new String(headerArray);
        Gson gson = new Gson();
        ContentViews contentViews = gson.fromJson(json, ContentViews.class);
        ActorSelection similaritor = getContext().actorSelection(ActorPaths.getPathToSimilaritor());
        for (ContentView contentView : contentViews) {
            SimilarContentViewPeerAlert similarPeerAlert = new SimilarContentViewPeerAlert(contentView);
            similaritor.tell(similarPeerAlert, getSelf());
        }
    }
}
