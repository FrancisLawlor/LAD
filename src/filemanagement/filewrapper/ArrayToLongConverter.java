package filemanagement.filewrapper;

import java.nio.ByteBuffer;

public class ArrayToLongConverter {
	public static byte[] longToByteArray(long num) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(num);
		
		return buffer.array();
	}
	
	public static long byteArrayToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes);
		buffer.flip();
		
		return buffer.getLong();
	}
}
