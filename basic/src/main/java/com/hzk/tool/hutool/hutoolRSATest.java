package com.hzk.tool.hutool;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 差异点
 * 1、hutool的base64编解码器cn.hutool.core.codec.Base64Encoder与苍穹使用的org.apache.commons.codec.binary.Base64不一致
 * 2、hutool的解密cn.hutool.crypto.asymmetric.AsymmetricCrypto#doFinal内部实现了分段解密，与市面上常规逻辑不同，苍穹无法兼容
 */
public class hutoolRSATest {
    private static String AppId = "jindie";
    private static String Secret = "34041de7a9932035fa766b0ab4e25f4df0737d48";

    public static String getSecret() {
        return Secret;
    }

    public static String getAppId() {
        return AppId;
    }

    /**
     * 类型
     */
    public static String ENCRYPT_TYPE = "RSA";

    /**
     * 获取公钥的key
     */
    private static String PUBLIC_KEY = null;

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAog5aFyQ8W1diTbBkmcT9QjMHC4m4fi1csI5184GVDylPZ+VJ057EVk+T1IzdGE/yw0wXHgAiSaq5fqqnFR6A8wIDAQABAkAb6i74l4RSE55aVY0us7EQayvGgAWornWt8Aw/Us3zW1q1WFVzwrLFG54mNrOxkB2ZixS9LfLWGgSdirJNprWJAiEA4/h/QWvNILEEYYdWFR2YJU6DL7PtaQT9Ja+oj82Ek70CIQC1+yfVpwQg9n23Asqo3FABqo8d4OJ9Ty5Gg04ywZpabwIgBqv5HXMIN9K2bBxH2qpZD45yrGy5n/8zYWz4o+zB2okCIQCsYbzaJdtDXjq3oW1Xh1pCOc5X/y6MhPTQ1pC8g6gzAQIgBFrSZLTyIwAnCEwAIKqdzhz1gyshRxzXmgCKijOuNr0=";

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }

    public static void main(String[] args) throws Exception {
        // base64实现有差异
        String a = "abcd";
        String encode = Base64Encoder.encode(a);
        byte[] decode = Base64Decoder.decode(encode);

        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        Object encode1 = base64.encode(a.getBytes("utf-8"));
        Object decode1 = base64.decode(encode1);
        // commons-codec解密hutool的base64编码
        Object decode2 = base64.decode(decode);

        String text = "jindie34041de7a9932035fa766b0ab4e25f4df0737d481723606514f9a78184-82a0-41f3-9f18-b9374cbe00e7{\"data\":{\"customer_dl\":0,\"customer_dm\":\"111sdf\",\"customer_dmtm\":\"111s********1sdf\",\"customer_id\":17507,\"customer_lx\":\"CHN\",\"customer_mc\":\"金蝶测试-1\",\"customer_sfjtgs\":0,\"customer_sfwczc\":0},\"rquestid\":\"f9a78184-82a0-41f3-9f18-b9374cbe00e7\",\"timestamp\":1723606514}";
        String token = "UdgwK8vET1Wqj8wXGdaN00wZsNr5OQORoCnyBu01nCe9RvUqSIz0PgrkyEXQtzir4KtCDhH7hcXA6ymr5TKqmCV57yZJn919JqCLcf0wCi1pz6qEtNIQbbSOG/OkvYhh0Ft36bUtUYaLTcdgwAfN/EZK0BFfua+melSIV929r01Q8y1JZ3eNWmFufeemx+S9iHhZtCZbg6/25wwin2KSuwEh3S6Qr/E/IFKX93znprVUvVsRZKUUM5BbI+kY7pm6ZDC7rnRGEKGA6DRDFN/uAeRswu9Ptf2bCOyY6vZBmINKssvyhz9/mqp7pWsaEUXoP+YhDZRLbJ5V1aR3nnTVHUfeiajcJXzqfNK04G0+5dto/qY23G/8mV7RFF+QPZMdUk1x2IG7sAsv7cHmsweA+cPbMUDYtHhrk4eqObUAmbQPmbqSppZZpUHwF3v9GMriIIOe7Abd0bu7QRct95mbyLSqfK0X2no5MnhDNrIFnWRRRIdsSI3vs/984s0UAMSIilZoRU9V3sUwn3Wc7dAUBf8ul67CnLZyL9jvjrH/tAEjPZRHfZdwMAHEHvcNJ7M1s1P5UAQ1mg4b+faTAQS3gg==";
        byte[] decode3 = Base64Decoder.decode(token);
        String encode3 = new String(base64.encode(decode3));

        byte[] decode4 = Base64Decoder.decode(PRIVATE_KEY);
        String encode4 = new String(base64.encode(decode4));


        String detoken = decrypt(token, PRIVATE_KEY);
        System.out.println(detoken);
        boolean flag = text.equals(detoken);
        System.out.println(flag);
    }

    public static Map<String, String> generateKeyPair() {
        try {
            KeyPair pair = SecureUtil.generateKeyPair(ENCRYPT_TYPE, 2048);
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            // 获取 公钥和私钥 的 编码格式（通过该 编码格式 可以反过来 生成公钥和私钥对象）
            byte[] pubEncBytes = publicKey.getEncoded();
            byte[] priEncBytes = privateKey.getEncoded();

            // 把 公钥和私钥 的 编码格式 转换为 Base64文本 方便保存
            String pubEncBase64 = Base64.getEncoder().encodeToString(pubEncBytes);
            String priEncBase64 = Base64.getEncoder().encodeToString(priEncBytes);

            Map<String, String> map = new HashMap<String, String>(2);
            map.put(PUBLIC_KEY, pubEncBase64);
            map.put(PRIVATE_KEY, priEncBase64);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥
     */
    public static String encrypt(String content, String publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String decrypt(String content, String privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
