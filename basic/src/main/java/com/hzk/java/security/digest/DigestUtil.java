package com.hzk.java.security.digest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Base64;

public class DigestUtil {

    static {
        // 注册 BouncyCastle 提供 SM3 支持
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 通用摘要方法
     */
    public static byte[] digest(String algorithm, byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("Digest error with algorithm: " + algorithm, e);
        }
    }

    /**
     * 摘要并转 HEX
     */
    public static String digestHex(String algorithm, String input) {
        try {
            byte[] bytes = digest(algorithm, input.getBytes("UTF-8"));
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 摘要并转 Base64
     */
    public static String digestBase64(String algorithm, String input) {
        try {
            byte[] bytes = digest(algorithm, input.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件摘要
     */
    public static String digestFileHex(String algorithm, File file) {
        try (InputStream is = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[8192];
            int n;
            while ((n = is.read(buffer)) != -1) {
                md.update(buffer, 0, n);
            }
            return bytesToHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException("File digest error", e);
        }
    }

    // 快捷方法
    public static String md5(String input) {
        return digestHex("MD5", input);
    }

    public static String sha1(String input) {
        return digestHex("SHA-1", input);
    }

    public static String sha256(String input) {
        return digestHex("SHA-256", input);
    }

    public static String sha512(String input) {
        return digestHex("SHA-512", input);
    }

    public static String sha3_256(String input) {
        return digestHex("SHA3-256", input);
    }

    // 🔥 SM3 算法（BouncyCastle 提供）
    public static String sm3(String input) {
        return digestHex("SM3", input);
    }

    /**
     * byte[] 转 HEX
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

