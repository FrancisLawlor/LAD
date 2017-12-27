package filemanagement.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import content.core.ContentFile;

/**
 * Writes files to disk
 *
 */
public class FileManager {
    private static final String BACKUP_DIR = "./files/";
    
    /**
     * Writes a media file to the temporary viewing directory
     * @param fileName
     * @param fileFormat
     * @param media
     */
    public static void writeMediaFile(String fileName, String fileFormat, byte[] media) throws IOException {
        String filepath = FileManager.getLocalFileStorageDirectoryPath() + fileName + "." + fileFormat;
        FileManager.writeFile(filepath, media);
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
     * Gets the temporary viewing directory for media files
     * @return
     * @throws IOException
     */
    private static String getLocalFileStorageDirectoryPath() throws IOException {
        try {
            FileReader configFile = new FileReader(FileConstants.CONFIG_FILE_NAME);
            
            Properties props = new Properties();
            props.load(configFile);
            
            String localFilesDirectory = props.getProperty(FileConstants.DIRECTORY_KEY);
            configFile.close();
            return localFilesDirectory;
        }
        catch (IOException e) {
            return BACKUP_DIR;
        }
    }
    
    /**
     * Writes the full Content File, not just the media segment, to the temporary viewing directory
     * Backup if Database is unavailable
     * @param contentFile
     * @throws IOException
     */
    public static void writeContentFile(ContentFile contentFile) throws IOException {
        String key = contentFile.getContent().getId();
        String filepath = FileManager.getLocalFileStorageDirectoryPath() + key;
        FileManager.writeFile(filepath, contentFile.getBytes());
    }
}
