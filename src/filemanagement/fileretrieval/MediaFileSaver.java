package filemanagement.fileretrieval;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import filemanagement.core.FileConstants;

/**
 * Writes files to disk
 *
 */
public class MediaFileSaver {
    private static final String MEDIA_DIR = "./files/";
    
    /**
     * Writes a media file to the temporary media viewing directory
     * @param fileName
     * @param fileFormat
     * @param media
     */
    public static void writeMediaFile(String fileName, String fileFormat, byte[] media) throws IOException {
        String filePath = MediaFileSaver.getFilePath(fileName, fileFormat);
        MediaFileSaver.writeFile(filePath, media);
    }
    
    /**
     * Get FilePath from File Name and File Format
     * @param fileName
     * @param fileFormat
     * @return
     * @throws IOException
     */
    private static final String getFilePath(String fileName, String fileFormat) throws IOException {
        String filePath = MediaFileSaver.getLocalFileStorageDirectoryPath() + fileName + "." + fileFormat;
        return filePath;
    }
    
    /**
     * Get File from File Name and File Format
     * @param fileName
     * @param fileFormat
     * @return
     * @throws IOException
     */
    public static final File getFile(String fileName, String fileFormat) throws IOException {
        return new File(MediaFileSaver.getFilePath(fileName, fileFormat));
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
     * Gets the temporary media viewing directory
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
            return MEDIA_DIR;
        }
    }
}
