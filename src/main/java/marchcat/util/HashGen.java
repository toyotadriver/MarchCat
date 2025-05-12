package marchcat.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashGen {
	public static String generatePassHash(String password) {
		MessageDigest md;
		
		byte[] passwordHash;
		try{
			md = MessageDigest.getInstance("SHA-256");
			
			passwordHash = md.digest(password.getBytes(StandardCharsets.UTF_8));
			
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
}
