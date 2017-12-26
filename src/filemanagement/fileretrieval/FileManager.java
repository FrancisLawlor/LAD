package filemanagement.fileretrieval;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import content.core.ContentFile;

/**
 * Writes and Reads content files
 *
 */
public class FileManager {
    private static final String OUT_DIR = "./files/";
    
    public static void writeContentFile(ContentFile contentFile) throws IOException {
        String filename = OUT_DIR + contentFile.getContent().getId();
        File file = new File(filename);
        OutputStream out = new FileOutputStream(file);
        byte[] bytes = contentFile.getBytes();
        out.write(bytes, 0, bytes.length);
        out.close();
    }
}
