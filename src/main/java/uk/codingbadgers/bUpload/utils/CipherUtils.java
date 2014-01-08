package uk.codingbadgers.bUpload.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.net.util.Base64;

public class CipherUtils {

	private static Cipher cipher;
	private static SecretKey myDesKey;
 
	static {
		try {
			DESKeySpec dks = new DESKeySpec(("db51d01dcbd534680e1c79254c6595cc70baec1f" + System.getProperty("user.name")).getBytes("ISO-8859-1"));
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
			myDesKey = skf.generateSecret(dks);
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	    } catch (Exception e) {
	    	e.printStackTrace();
	   }
	}
	
	private CipherUtils() {} // Utils class
	
	public static String encryptString(String string) {
		try {
			byte[] text = string.getBytes("ISO-8859-1");
			cipher.init(Cipher.ENCRYPT_MODE, myDesKey);
			byte[] textEncrypted = cipher.doFinal(text);
			return Base64.encodeBase64String(textEncrypted);
		} catch (Exception e) {
			e.printStackTrace();
			return string;
		}
	}

	public static String decryptString(String string) {
		try {
			byte[] decodedBytes = Base64.decodeBase64(string);
			cipher.init(Cipher.DECRYPT_MODE, myDesKey);
			return new String(cipher.doFinal(decodedBytes), "ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			return string;
		}
	}

}
