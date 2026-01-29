package marchcat.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashGen {
	public static String generateStringHash(String string) {
		MessageDigest md;
		
		byte[] passwordHash;
		try{
			md = MessageDigest.getInstance("SHA-256");
			
			passwordHash = md.digest(string.getBytes(StandardCharsets.UTF_8));
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
			return "";
			
		}
		StringBuilder outputString = new StringBuilder("");
		for(int i = 0; i < passwordHash.length; i++) {
			String hex = Integer.toHexString(0xff & passwordHash[i]);
			if(hex.length() ==1 ) {
				outputString.append('0');
			}
			outputString.append(hex);
		}
		return outputString.toString();
		
		
	}
	
public static String generateISHash(InputStream is) {
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			
			byte[] buffer = new byte[1024];
			int bytesRead;
		
			while((bytesRead = is.read(buffer)) != -1) {
			md.update(buffer, 0, bytesRead);
			
			byte[] d = md.digest();
			
			StringBuilder hexString = new StringBuilder();
      for (byte b : d) {
          hexString.append(String.format("%02x", b));
      }

      return hexString.toString();
		}
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO: handle exception
		}
		
		return "NOHASHLOL";
	}
}
