package com.bonc.channel_edit.utils;

import java.security.MessageDigest;

public class SHA1 {

	@SuppressWarnings("static-access")
	public static String getSHAResult(String shaString){

		String sha1Str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(shaString.getBytes());
			byte[] digesta = md.digest();
			Base64 base64 = new Base64();
//			Base64Encoder base64 = new Base64Encoder();
			sha1Str = base64.encode(digesta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sha1Str;
	}
}
