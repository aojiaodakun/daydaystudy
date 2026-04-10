package com.hzk.tool.encrypt;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.io.BaseEncoding;

/**
 * AES加密工具类
 *
 * @author rd_xiaomou_zhang
 *
 */
public class AESUtils {

    private static final String AES = "AES";

    private static final String AES_CIPHER_MODE = "AES/GCM/NoPadding";

    private static final String DEFAULT_KEY = "jYq3iy67dNQUla9I";
    /**
     * @param key   秘钥
     * @param value 待加密内容
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String value) {
        try {
            return base64Encode(aesEncryptToBytes(key, value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encrypt(String value) {
        try{
            return base64Encode(aesEncryptToBytes(DEFAULT_KEY, value));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }



    private static String base64Encode(byte[] bytes) {
        return BaseEncoding.base64Url().encode(bytes);
    }

    private static byte[] aesEncryptToBytes(String encryptKey,String content) throws Exception {
        return generateCipher(Cipher.ENCRYPT_MODE, content.getBytes(StandardCharsets.UTF_8), encryptKey);
    }


    public static String decrypt(String encryptValue)  {
        try {
            return aesDecryptByBytes(DEFAULT_KEY,base64Decode(encryptValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param encryptValue 待解密内容
     * @param key          秘钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String key,String encryptValue) {
        try {
            return aesDecryptByBytes(key,base64Decode(encryptValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static byte[] base64Decode(String base64Code) {
        return base64Code == null ? null : BaseEncoding.base64Url().decode(base64Code);
    }
    private static String aesDecryptByBytes (String decryptKey, byte[] encryptBytes) throws Exception {
        byte[] decryptBytes = generateCipher(Cipher.DECRYPT_MODE, encryptBytes, decryptKey);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    private static byte[] generateCipher ( int mode, byte[] encryptBytes, String decryptKey)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_CIPHER_MODE);
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(decryptKey.getBytes(StandardCharsets.UTF_8));
        kgen.init(128, random);

        byte[] bytesIV = new byte[16];
        random.nextBytes(bytesIV);
        cipher.init(mode, new SecretKeySpec(decryptKey.getBytes(StandardCharsets.UTF_8), AES),
                new GCMParameterSpec(128, bytesIV));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return decryptBytes;
    }

    public static String toHex16 ( byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


}