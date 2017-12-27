package tests.content.retrieve;

import filemanagement.filewrapper.ArrayToLongConverter;
import filemanagement.filewrapper.FileUnwrapper;

public class TestHeaderMediaFile {
    static final String TEST = "A1B2C3D4E5F6G7H8I9J10";
    private static final long headerLength = 23;
    
    public static void main(String[] args) {
        byte[] file = getHeaderMediaFile();
        
        // Check Header Array
        System.out.println("Recovered Header Array:");
        byte[] headerArrayTest = FileUnwrapper.extractHeaderArray(file);
        for (int i = 0; i < headerArrayTest.length; i++) {
            System.out.println((int)headerArrayTest[i]);
        }
        
        // Check Media Array
        System.out.println("Recovered Media Array to String:");
        byte[] mediaArrayTest = FileUnwrapper.extractFileArray(file);
        System.out.println(new String(mediaArrayTest));
    }
    
    static byte[] getHeaderMediaFile() {
        byte[] headerArray = new byte[(int)headerLength];
        for (int i = 0; i < headerLength; i++) {
            headerArray[i] = (byte) i;
        }
        
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
        for (int i = 0; i < headerArrayTest.length; i++) {
            buffer.append(((int)headerArrayTest[i]) + "_");
        }
        
        byte[] mediaArrayTest = FileUnwrapper.extractFileArray(contentFileBytes);
        String mediaString = new String(mediaArrayTest);
        buffer.append(mediaString);
        
        return buffer.toString();
    }
}
