package filemanagement.filewrapper;

public class FileUnwrapper {
	public byte[] extractHeaderArray(byte[] appendedFilesArray) {
		byte[] headerLengthArray = new byte[8];
		
		for (int i = 0; i < 8; i++) {
			headerLengthArray[i] = appendedFilesArray[i];
		}
		
		long headerEndIndex = ArrayToLongConverter.byteArrayToLong(headerLengthArray) + 8;
		
		byte[] headerArray = new byte[(int) (headerEndIndex - 8)];
		
		for (int i = Long.BYTES, j = 0; i < headerEndIndex; i++, j++) {
			headerArray[j] = appendedFilesArray[i];
		}
		
		return headerArray;
	}
	
	public byte[] extractFileArray(byte[] appendedFilesArray) {
		byte[] headerLengthArray = new byte[8];
		
		for (int i = 0; i < 8; i++) {
			headerLengthArray[i] = appendedFilesArray[i];
		}
		
		int mediaStartIndex = (int) (ArrayToLongConverter.byteArrayToLong(headerLengthArray) + 8);
		
		byte[] mediaArray = new byte[(int) (appendedFilesArray.length - mediaStartIndex)];
		
		for (int i = mediaStartIndex, j = 0; i < appendedFilesArray.length; i++, j++) {
			mediaArray[j] = appendedFilesArray[i];
		}
		
		return mediaArray;
	}
}
