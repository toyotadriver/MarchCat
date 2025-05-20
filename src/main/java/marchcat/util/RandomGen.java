package marchcat.util;

import java.util.Random;

public class RandomGen {
	
	public static String randomString(int length) {
		StringBuilder randomString = new StringBuilder();
		
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		
		Random random = new Random();
		
		for(int i = 0; i < length; i++) {
			randomString.append(characters.charAt(random.nextInt(61)));
		}
		
		return randomString.toString();
	}
}
