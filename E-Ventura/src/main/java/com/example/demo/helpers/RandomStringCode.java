package com.example.demo.helpers;

import java.util.Random;

public class RandomStringCode {
	public static String generateRandomAlphaNumeric(int length) {
	    String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        int index = random.nextInt(CHARACTERS.length());
	        sb.append(CHARACTERS.charAt(index));
	    }
	    return sb.toString();
	}
}
