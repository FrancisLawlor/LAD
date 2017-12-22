package filemanagement.fileretrieval;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;


public class FileRetriever {
	public static File downloadFile(String remoteFileURL, String localSaveDirectory) throws IOException {
		URL remoteLocationURL = new URL(remoteFileURL);
		int fileBytes;
		byte[] bytes = new byte[1024];
		
		String localFilePath = buildLocalFilePath(parseFileName(remoteFileURL), localSaveDirectory);
	 
		InputStream remoteFileInputStream = remoteLocationURL.openConnection().getInputStream();
		File retrievedFile = new File(localFilePath);

		OutputStream localFileOutputStream = new FileOutputStream(retrievedFile);
	 
		while ((fileBytes = remoteFileInputStream.read(bytes)) > 0) {
			localFileOutputStream.write(bytes, 0, fileBytes);
		}
		
		localFileOutputStream.close();
		remoteFileInputStream.close();
		
		return retrievedFile;
	}
	
	private static String buildLocalFilePath(String remoteFileURL, String localSaveDirectory) {
		StringBuilder sb = new StringBuilder();
		sb.append(localSaveDirectory);
		sb.append("/");
		sb.append(remoteFileURL);
		
		return sb.toString();
	}
	
	private static String parseFileName(String remoteFileURL) {
		return remoteFileURL.substring(remoteFileURL.lastIndexOf('/') + 1, remoteFileURL.length());
	}
}