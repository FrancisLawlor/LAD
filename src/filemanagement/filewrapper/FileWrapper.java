package filemanagement.filewrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWrapper {
	private static byte[] convertFileToByteArray(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		
		return Files.readAllBytes(path);		
	}
	
	public static byte[] mergeHeaderFileWithMediaFile (String headerFilePath, String mediaFilePath) throws IOException {
		byte[] headerArray = convertFileToByteArray(headerFilePath);
		byte[] mediaArray = convertFileToByteArray(mediaFilePath);
		
		long headerLength = headerArray.length;
		
		byte[] appendedFilesArray = new byte[headerArray.length + mediaArray.length + Long.BYTES];
		byte[] headerLengthAsByteArray = ArrayToLongConverter.longToByteArray(headerLength);
		
		int i;
		
		for (i = 0; i < Long.BYTES; i++) {
			appendedFilesArray[i] = headerLengthAsByteArray[i];
		}
		
		for (int j = 0; j < headerArray.length; j++) {
			appendedFilesArray[i] = headerArray[j];
			i++;
		}
		
		for (int j = 0; j < mediaArray.length; j++) {
			appendedFilesArray[i] = mediaArray[j];
			i++;
		}
		
		return appendedFilesArray;
	}
}
