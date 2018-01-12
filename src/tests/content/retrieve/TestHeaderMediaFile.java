package tests.content.retrieve;

import com.google.gson.Gson;

import content.frame.core.Content;
import content.view.core.ContentView;
import content.view.core.ContentViews;
import content.view.core.Rating;
import content.view.core.ViewingTime;
import filemanagement.filewrapper.ArrayToLongConverter;
import filemanagement.filewrapper.FileUnwrapper;
import peer.frame.core.UniversalId;

public class TestHeaderMediaFile {
    static final String TEST = "A1B2C3D4E5F6G7H8I9J10";
    
    public static void main(String[] args) {
        byte[] file = getHeaderMediaFile();
        
        // Check Header Array
        System.out.println("Recovered Header Array:");
        byte[] headerArrayTest = FileUnwrapper.extractHeaderArray(file);
        System.out.println(new String(headerArrayTest));
        
        // Check Media Array
        System.out.println("Recovered Media Array to String:");
        byte[] mediaArrayTest = FileUnwrapper.extractFileArray(file);
        System.out.println(new String(mediaArrayTest));
    }
    
    static byte[] getHeaderMediaFile() {
        Content content = new Content("UniqueId", "Filename", "FileFormat", 10);
        ContentViews contentViews = new ContentViews(content);
        ContentView contentView = new ContentView(content, new UniversalId("localhost:10010"));
        contentView.recordView(new ViewingTime(5));
        contentView.setRating(new Rating(4.0));
        contentViews.addContentView(contentView);
        contentView = new ContentView(content, new UniversalId("localhost:10011"));
        contentView.recordView(new ViewingTime(5));
        contentView.setRating(new Rating(4.0));
        contentViews.addContentView(contentView);
        contentView = new ContentView(content, new UniversalId("localhost:10012"));
        contentView.recordView(new ViewingTime(5));
        contentView.setRating(new Rating(4.0));
        contentViews.addContentView(contentView);
        contentView = new ContentView(content, new UniversalId("localhost:10013"));
        contentView.recordView(new ViewingTime(5));
        contentView.setRating(new Rating(4.0));
        contentViews.addContentView(contentView);
        contentView = new ContentView(content, new UniversalId("localhost:10014"));
        contentView.recordView(new ViewingTime(5));
        contentView.setRating(new Rating(4.0));
        contentViews.addContentView(contentView);
        contentView = new ContentView(content, new UniversalId("localhost:10015"));
        Gson gson = new Gson();
        String json = gson.toJson(contentViews);
        
        byte[] headerArray = json.getBytes();
        long headerLength = headerArray.length;
        
        byte[] mediaArray = TEST.getBytes();
        
        int fileLength = Long.BYTES + headerArray.length + mediaArray.length;
        byte[] file = new byte[fileLength];
        
        byte[] headerLengthAsByteArray = ArrayToLongConverter.longToByteArray(headerLength);
        
        int i;
        
        for (i = 0; i < Long.BYTES; i++) {
            file[i] = headerLengthAsByteArray[i];
        }
        
        for (int j = 0; j < headerArray.length; j++) {
            file[i] = headerArray[j];
            i++;
        }
        
        for (int j = 0; j < mediaArray.length; j++) {
            file[i] = mediaArray[j];
            i++;
        }
        return file;
    }
    
    static String getContentFileBytesAsString(byte[] contentFileBytes) {
        StringBuffer buffer = new StringBuffer();
        
        byte[] headerArrayTest = FileUnwrapper.extractHeaderArray(contentFileBytes);
        buffer.append(new String(headerArrayTest));
        
        byte[] mediaArrayTest = FileUnwrapper.extractFileArray(contentFileBytes);
        String mediaString = new String(mediaArrayTest);
        buffer.append(mediaString);
        
        return buffer.toString();
    }
}
