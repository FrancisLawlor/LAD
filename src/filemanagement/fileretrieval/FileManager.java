package filemanagement.fileretrieval;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import akka.actor.ActorRef;
import content.core.Content;
import content.core.ContentFile;
import content.similarity.SimilarContentViewPeers;
import content.view.ContentView;
import filemanagement.core.FileConstants;
import peer.graph.distributedmap.PeerWeightedLink;

/**
 * Writes files to disk
 *
 */
public class FileManager {
    private static final String BACKUP_DIR = "./files/";
    private static final String CONTENT_FILE_EXTENSION = "lad";
    
    /**
     * Get File from File Name and File Format
     * @param fileName
     * @param fileFormat
     * @return
     * @throws IOException
     */
    public static final File getFile(String fileName, String fileFormat) throws IOException {
        return new File(FileManager.getFilePath(fileName, fileFormat));
    }
    
    /**
     * Get FilePath from File Name and File Format
     * @param fileName
     * @param fileFormat
     * @return
     * @throws IOException
     */
    private static final String getFilePath(String fileName, String fileFormat) throws IOException {
        String filePath = FileManager.getLocalFileStorageDirectoryPath() + fileName + "." + fileFormat;
        return filePath;
    }
    
    /**
     * Writes a media file to the files directory
     * @param fileName
     * @param fileFormat
     * @param media
     */
    public static void writeMediaFile(String fileName, String fileFormat, byte[] media) throws IOException {
        String filePath = FileManager.getFilePath(fileName, fileFormat);
        FileManager.writeFile(filePath, media);
    }
    
    /**
     * Reusable helper to write a byte array to a full filepath
     * @param filepath
     * @param bytes
     * @throws IOException
     */
    private static void writeFile(String filepath, byte[] bytes) throws IOException {
        File file = new File(filepath);
        OutputStream out = new FileOutputStream(file);
        out.write(bytes, 0, bytes.length);
        out.close();
    }
    
    /**
     * Gets the files directory
     * @return
     * @throws IOException
     */
    private static String getLocalFileStorageDirectoryPath() throws IOException {
        try {
            FileReader configFile = new FileReader(FileConstants.CONFIG_FILE_NAME);
            
            Properties props = new Properties();
            props.load(configFile);
            
            String localFilesDirectory = props.getProperty(FileConstants.DIRECTORY_KEY) + "/";
            configFile.close();
            return localFilesDirectory;
        }
        catch (IOException e) {
            return BACKUP_DIR;
        }
    }
    
    /**
     * Writes the full Content File, not just the media segment, to the files directory
     * @param contentFile
     * @throws IOException
     */
    public static void writeContentFile(ContentFile contentFile) throws IOException {
        String key = contentFile.getContent().getId();
        String filepath = FileManager.getFilePath(key, CONTENT_FILE_EXTENSION);
        FileManager.writeFile(filepath, contentFile.getBytes());
    }
    
    /**
     * 
     * @param content
     */
    public static void checkContentFileExists(Content content) {
        
    }
    
    /**
     * 
     * @param content
     */
    public static void readContentFile(Content content) {
        
    }
    
    /**
     * 
     * @param contentView
     */
    public static void contentViewAddition(ContentView contentView) {
        
    }
    
    /**
     * 
     * @param peerWeightedLink
     */
    public static void backupPeerLink(PeerWeightedLink peerWeightedLink) {
        
    }
    
    /**
     * 
     * @param requester
     */
    public static void requestBackedUpPeerLinks(ActorRef requester) {
        
    }
    
    /**
     * 
     * @param similarContentViewPeers
     */
    public static void backupSimilarContentViewPeers(SimilarContentViewPeers similarContentViewPeers) {
        
    }
    
    /**
     * 
     * @param requester
     */
    public static void requestBackedUpSimilarContentViewPeers(ActorRef requester) {
        
    }
}
