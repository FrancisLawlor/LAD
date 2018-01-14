package security.encryption;

import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import filemanagement.core.FileConstants;

public class Encrypter {
	private final static String MD5 = "MD5";
	private final static String DES = "DES";
	private final static String UTF8 = "UTF8";
	private SecretKey key;
	
	public Encrypter() throws NoSuchAlgorithmException, IOException {
		FileReader configFile = new FileReader(FileConstants.CONFIG_FILE_NAME);
		
		Properties props = new Properties();
		props.load(configFile);
		
		byte[] decodedKey = Base64.decodeBase64(props.getProperty(FileConstants.ENCRYPTION_KEY));
		this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, DES);
	}
	
	public static String hash(String stringToBeHashed) {
		try {
			java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance(MD5);
			byte[] byteArray = messageDigest.digest(stringToBeHashed.getBytes());
			
			StringBuilder stringBuilder = new StringBuilder();
			
			for (int i = 0; i < byteArray.length; ++i) {
				stringBuilder.append(Integer.toHexString((byteArray[i] & 0xFF) | 0x100).substring(1,3));
			}
			
			return stringBuilder.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			
		}
		
		return null;
	}
	
	public String encrypt(String str) {
		try {
			Cipher encryptionCipher = Cipher.getInstance(DES);
			encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
			  
			byte[] utf8 = str.getBytes(UTF8);
			byte[] encryptedStringAsBytes = encryptionCipher.doFinal(utf8);
			encryptedStringAsBytes = Base64.encodeBase64(encryptedStringAsBytes);
			
			return new String(encryptedStringAsBytes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String decrypt(String str) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher decryptionCipher = Cipher.getInstance(DES);

		decryptionCipher = Cipher.getInstance(DES);
		decryptionCipher.init(Cipher.DECRYPT_MODE, key);
		
		try {
			byte[] dec = Base64.decodeBase64(str.getBytes());
			byte[] utf8 = decryptionCipher.doFinal(dec);
		
			return new String(utf8, UTF8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
