/**
 *
 */
package com.bonc.channel_edit.utils;

import android.util.Base64;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Administrator
 */
public class AES {
    public final static String CharSet = "UTF-8";
    private static String Key = "";
    private static Cipher Encoder = null;
    private static Cipher Decoder = null;

    public static String getKey() {
//		if (TextUtils.isEmpty(Key)) {
//			Key = XConstants.key;
//		}
        return Key;
    }

    public static void setKey(String key) {
        AES.Key = key;
    }

    public static String encode(String plain) {
        try {
            // if (plain == null) {
            // return null;
            // }
            Cipher cipher = getEncoder();
            byte[] res = cipher.doFinal(plain.getBytes(CharSet));
            printHexString(res);
            // byte[] res = plain.getBytes(CharSet);
            res = Base64.encode(res, Base64.NO_WRAP);// Base64.encode(res,NO_WRAP
            // Base64.DEFAULT);
            String rtn = new String(res, 0, res.length, CharSet);

            // rtn = rtn.replace("\n", "");
            return rtn;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            // String s2 = Long.toString(Long.parseLong(hex, 16));
            BigInteger bi = new BigInteger(hex, 16);
            Long il = bi.longValue();
            System.out.print(hex.toUpperCase());
        }
    }

    public static String decode(String cryp) {
        try {
            // if (cryp == null) {
            // return null;
            // }
            byte[] res = Base64.decode(cryp.getBytes(CharSet), Base64.NO_WRAP);
            System.out.println(res);
            Cipher cipher = getDecoder();
            res = cipher.doFinal(res);
            return new String(res, 0, res.length, CharSet);
            // return cryp;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static Cipher getEncoder() throws Exception {
        if (Encoder == null) {
            String key = getKey();
            byte[] bs = key.getBytes(CharSet);
            SecretKeySpec skeySpec = new SecretKeySpec(bs, "AES");// "AES/ECB/PKCS5Padding");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding"); // ZeroBytePadding
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            Encoder = cipher;
        }
        return Encoder;
    }

    private static Cipher getDecoder() throws Exception {
        if (Decoder == null) {
            String key = getKey();
            // String key="3247c80c653aedb3df8fa93faf6fd80f";
            byte[] bs = key.getBytes(CharSet);
            SecretKeySpec skeySpec = new SecretKeySpec(bs, "AES");// "AES/ECB/PKCS5Padding");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            Decoder = cipher;
        }
        return Decoder;
    }

}