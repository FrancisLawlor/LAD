package peer.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import com.google.gson.Gson;

import akka.actor.ActorRef;
import content.core.Content;
import content.core.ContentFile;
import content.core.ContentFileExistenceRequest;
import content.core.ContentFileExistenceResponse;
import content.core.ContentFileRequest;
import content.core.ContentFileResponse;
import content.retrieve.RetrievedContentFile;
import content.similarity.SimilarContentViewPeers;
import content.view.ContentView;
import content.view.ContentViewAddition;
import content.view.ContentViews;
import filemanagement.filewrapper.FileUnwrapper;
import filemanagement.filewrapper.FileWrapper;
import peer.core.PeerToPeerActor;
import peer.core.PeerToPeerActorInit;
import peer.core.xcept.ImproperlyStoredContentFileException;
import peer.core.xcept.UnknownMessageException;
import peer.graph.distributedmap.PeerWeightedLink;

/**
 * Database handler actor that stores data in Properties files
 *
 */
public class Databaser extends PeerToPeerActor {
    private static final String FILE_EXTENSION = ".properties";
    private static final String DATA_DIR = "./data/";
    private static final String PEER_LINKS_FILENAME = "PeerLinks" + FILE_EXTENSION;
    private static final String SIMILAR_CONTENT_VIEW_PEERS_FILENAME = "SimilarContentViewPeers" + FILE_EXTENSION;
    private static final String CONTENT_FILE_EXTENSION = ".lad";
    
    /**
     * Actor message processing
     */
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PeerToPeerActorInit) {
            PeerToPeerActorInit init = (PeerToPeerActorInit) message;
            this.initialisePeerToPeerActor(init);
        }
        else if (message instanceof ContentFileExistenceRequest) {
            ContentFileExistenceRequest request = (ContentFileExistenceRequest) message;
            this.processContentFileExistenceRequest(request);
        }
        else if (message instanceof ContentFileRequest) {
            ContentFileRequest request = (ContentFileRequest) message;
            this.processContentFileRequest(request);
        }
        else if (message instanceof RetrievedContentFile) {
            RetrievedContentFile retrievedContentFile = (RetrievedContentFile) message;
            this.processRetrievedContentFile(retrievedContentFile);
        }
        else if (message instanceof ContentViewAddition) {
            ContentViewAddition addition = (ContentViewAddition) message;
            this.processContentViewAddition(addition);
        }
        else if (message instanceof BackupPeerLinkRequest) {
            BackupPeerLinkRequest request = (BackupPeerLinkRequest) message;
            this.processBackupPeerLinkRequest(request);
        }
        else if (message instanceof BackupSimilarContentViewPeersRequest) {
            BackupSimilarContentViewPeersRequest request = (BackupSimilarContentViewPeersRequest) message;
            this.processBackupSimilarContentViewPeersRequest(request);
        }
        else if (message instanceof BackedUpPeerLinksRequest) {
            BackedUpPeerLinksRequest request = (BackedUpPeerLinksRequest) message;
            this.processBackedUpPeerLinksRequest(request);
        }
        else if (message instanceof BackedUpSimilarContentViewPeersRequest) {
            BackedUpSimilarContentViewPeersRequest request = (BackedUpSimilarContentViewPeersRequest) message;
            this.processBackedUpSimilarContentViewPeersRequest(request);
        }
        else {
            throw new UnknownMessageException();
        }
    }
    
    /**
     * Checks database to see if there is a file stored matching this Content object's description
     * @param request
     */
    protected void processContentFileExistenceRequest(ContentFileExistenceRequest request) {
        Content content = request.getContent();
        boolean exists = checkContentFileExists(content);
        ContentFileExistenceResponse response = new ContentFileExistenceResponse(content, exists);
        ActorRef requester = getSender();
        requester.tell(response, getSelf());
    }
    
    /**
     * Checks if a content file exists
     * @param content
     * @return
     */
    private static boolean checkContentFileExists(Content content) {
        File file = new File(getFilePath(content));
        boolean exists = file.exists();
        return exists;
    }
    
    /**
     * Returns a content file to requesting actor if it exists in the database
     * @param request
     */
    protected void processContentFileRequest(ContentFileRequest request) throws IOException {
        Content content = request.getContent();
        ContentFile contentFile = getContentFile(content);
        ContentFileResponse response = new ContentFileResponse(contentFile);
        ActorRef requester = getSender();
        requester.tell(response, getSelf());
    }
    
    /**
     * Reads a Content File from disk
     * @param filepath
     * @return
     */
    private static ContentFile getContentFile(Content content) throws IOException {
        Path path = Paths.get(getFilePath(content));
        byte[] appendedFilesArray = Files.readAllBytes(path);
        ContentFile contentFile = new ContentFile(content, appendedFilesArray);
        return contentFile;
    }
    
    /**
     * Get FilePath from File Name and File Format
     * @param fileName
     * @param fileFormat
     * @return
     * @throws IOException
     */
    private static final String getFilePath(Content content) {
        String filePath = DATA_DIR + content.getId() + "." + CONTENT_FILE_EXTENSION;
        return filePath;
    }
    
    /**
     * Writes a retrieved content file to the database
     */
    protected void processRetrievedContentFile(RetrievedContentFile retrievedContentFile) throws IOException {
        ContentFile contentFile = retrievedContentFile.getContentFile();
        writeContentFile(contentFile);
    }
    
    /**
     * Writes a content file to disk
     * @param filepath
     * @param bytes
     * @throws IOException
     */
    private static void writeContentFile(ContentFile contentFile) throws IOException {
        byte[] bytes = contentFile.getBytes();
        String filepath = getFilePath(contentFile.getContent());
        writeBytesToFile(filepath, bytes);
    }
    
    /**
     * Write bytes to file on disk
     * @param filepath
     * @param bytes
     * @throws IOException
     */
    private static void writeBytesToFile(String filepath, byte[] bytes) throws IOException {
        File file = new File(filepath);
        OutputStream out = new FileOutputStream(file, false);
        out.write(bytes, 0, bytes.length);
        out.close();
    }
    
    /**
     * Adds a Content View to the ContentViews header in the relevant Content File stored in the database
     * @param contentViewAddition
     */
    protected void processContentViewAddition(ContentViewAddition contentViewAddition) throws IOException {
        ContentView contentView = contentViewAddition.getContentView();
        Content content = contentView.getContent();
        ContentViews contentViews = getContentViews(content);
        contentViews.addContentView(contentView);
        setContentViews(contentViews);
    }
    
    /**
     * Gets the Content Views from the header of the content file stored on disk
     * @return
     */
    private static ContentViews getContentViews(Content content) throws IOException {
        Path path = Paths.get(getFilePath(content));
        byte[] appendedFilesArray = Files.readAllBytes(path);
        byte[] headerArray = FileUnwrapper.extractHeaderArray(appendedFilesArray);
        String json = new String(headerArray);
        Gson gson = new Gson();
        ContentViews contentViews = gson.fromJson(json, ContentViews.class);
        if (!contentViews.getContent().equals(content)) throw new ImproperlyStoredContentFileException(contentViews.getContent(), content);
        return contentViews;
    }
    
    /**
     * Rewrites the header with the media segment of the content file back to disk
     * @param contentViews
     */
    private static void setContentViews(ContentViews contentViews) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(contentViews);
        byte[] headerArray = json.getBytes();
        Content content = contentViews.getContent();
        Path path = Paths.get(getFilePath(content));
        byte[] appendedFilesArray = Files.readAllBytes(path);
        byte[] mediaArray = FileUnwrapper.extractFileArray(appendedFilesArray);
        appendedFilesArray = FileWrapper.mergeHeaderDataWithMediaFile(headerArray, mediaArray);
        writeBytesToFile(getFilePath(content), appendedFilesArray);
    }
    
    /**
     * Will back up a link this peer has to other peers
     * @param request
     */
    protected void processBackupPeerLinkRequest(BackupPeerLinkRequest request) {
        PeerWeightedLink peerWeightedLink = request.getPeerWeightedLink();
        Properties prop = new Properties();
        OutputStream output = null;
        String filename = DATA_DIR + PEER_LINKS_FILENAME;
        Gson gson = new Gson();
        try {
            output = new FileOutputStream(filename, true);
            String key = peerWeightedLink.getLinkedPeerId().toString();
            String value = gson.toJson(peerWeightedLink);
            prop.setProperty(key, value);
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Will back up a set of peers who watched the same content
     * @param request
     */
    protected void processBackupSimilarContentViewPeersRequest(BackupSimilarContentViewPeersRequest request) {
        SimilarContentViewPeers similarContentViewPeers = request.getSimilarContentViewPeers();
        Properties prop = new Properties();
        OutputStream output = null;
        String filename = DATA_DIR + SIMILAR_CONTENT_VIEW_PEERS_FILENAME;
        Gson gson = new Gson();
        try {
            output = new FileOutputStream(filename, true);
            String key = similarContentViewPeers.getContent().getId();
            String value = gson.toJson(similarContentViewPeers);
            prop.setProperty(key, value);
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Will return all backed up peer links to the requester
     * @param request
     */
    protected void processBackedUpPeerLinksRequest(BackedUpPeerLinksRequest request) {
        String filename = DATA_DIR + PEER_LINKS_FILENAME;
        File file = new File(filename);
        if (file.exists()) {
            ActorRef requester = getSender();
            Properties prop = new Properties();
            InputStream input = null;
            Gson gson = new Gson();
            try {
                input = new FileInputStream(filename);
                prop.load(input);
                Enumeration<?> e = prop.propertyNames();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = prop.getProperty(key);
                    PeerWeightedLink peerWeightedLink = gson.fromJson(value, PeerWeightedLink.class);
                    requester.tell(peerWeightedLink, ActorRef.noSender());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    /**
     * Will return to the requester all backed up sets of peers who watched similar content
     * @param request
     */
    protected void processBackedUpSimilarContentViewPeersRequest(BackedUpSimilarContentViewPeersRequest request) {
        String filename = DATA_DIR + SIMILAR_CONTENT_VIEW_PEERS_FILENAME;
        File file = new File(filename);
        if (file.exists()) {
            ActorRef requester = getSender();
            Properties prop = new Properties();
            InputStream input = null;
            Gson gson = new Gson();
            try {
                input = new FileInputStream(filename);
                prop.load(input);
                Enumeration<?> e = prop.propertyNames();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = prop.getProperty(key);
                    SimilarContentViewPeers similarContentViewPeers = gson.fromJson(value, SimilarContentViewPeers.class);
                    requester.tell(similarContentViewPeers, ActorRef.noSender());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
