package com.hzk.tool.encrypt;

import java.security.MessageDigest;

public class SHA1Utils {

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private static final String ALGORITHM_SHA1 = "SHA-1";

    private static final String ALGORITHM_MD5 = "MD5";

    public static String sha1Encode(String srcStr){
        return hash(ALGORITHM_SHA1, srcStr);
    }

    public static String md5Encode(String srcStr){
        return hash(ALGORITHM_MD5, srcStr);
    }


    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

}
