package marchcat.util;

import java.util.Random;

public class RandomGen {
	
	public final static String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	public static String randomString(int length) {
		StringBuilder randomString = new StringBuilder();
		
		Random random = new Random();
		
		for(int i = 0; i < length; i++) {
			randomString.append(CHARACTERS.charAt(random.nextInt(61)));
		}
		
		return randomString.toString();
	}
}
