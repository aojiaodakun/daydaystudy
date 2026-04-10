package com.hzk.tool.encrypt;

public class EncryptHelper {

    /**
     * 1级加密
     * @param str
     * @return
     */
    public static String systemEncode(String str){
        String keyString = SystemKeyDemo.getKey();
        String prefix = keyString.split(",")[0];
        String key1 = keyString.split(",")[1];
        return prefix + AESUtils.encrypt(key1,str);
    }

    /**
     * 1级解密
     * @param str
     * @return
     */
    public static String systemDncode(String str){
        String keyString = SystemKeyDemo.getKey();
        String prefix = keyString.split(",")[0];
        String key1 = keyString.split(",")[1];
        String str1 = str.replace(prefix,"");
        return prefix + AESUtils.decrypt(key1,str1);
    }



    public static void main(String[] args) {
        // 系统密钥
        String value = "abc123";
        String encryptValue = systemEncode(value);
        System.out.println(encryptValue);
        String dncryptValue = systemDncode(encryptValue);
        System.out.println(dncryptValue);


        // 主管密钥


        // 用户密钥

    }

    /**
     * 主管加密
     * @param key
     * @param str
     * @return
     */
    public static String directorEncode(String key,String str){
        return AESUtils.encrypt(key,str);
    }

    /**
     * 主管解密
     * @param key
     * @param str
     * @return
     */
    public static String directorDecode(String key,String str){
        return AESUtils.decrypt(key,str);
    }

    /**
     * 用户加密
     * @param key
     * @param str
     * @return
     */
    public static String personEncode(String key,String str){
        return AESUtils.encrypt(key,str);
    }

    /**
     * 用户解密
     * @param key
     * @param str
     * @return
     */
    public static String personDecode(String key,String str){
        return AESUtils.decrypt(key,str);
    }

}
