package com.hzk.tool.encrypt;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 系统秘钥
 */
public class SystemKeyDemo {

    private final static int LEN_16 = 16;

    private final static int VERSION = 0;

    private final static int SPILT_INDEX = 6;

    private SystemKeyDemo(){}

//    private String[][] key = new String[][]{
//            {"f%p*M:","oEY2PNwBVRXQYqcEb","KtHORUFu1Dsgc9GsM"},
//            {"xp$EK:","7v0mR3VfOCJftzsoo","vNnOro1LAurISVOs9"}
//    };


    private static String[][] key = new String[][]{
        {"f%p*M:","L4CZwr7_GBxdnvu8bbdxl09HqaaBDErxmPM_6S_v-BUK6HMfTOiAwOnxx10RO0mWQG8="},
        {"xp$EK:","NKG365qmGDRigcewSdoUzldNi__8AmrxktkCyy3n-hoKqSy6BmeGwbQwQif-P7oGrOE="}
    };






    public static void main(String[] args) throws Exception{
        SystemKeyDemo demo = new SystemKeyDemo();
        String key = demo.generatorKey();
        System.out.println("系统密钥明文:" + key);
        String encryptKey = AESUtils.encrypt(key);
        System.out.println("系统密钥密文:" + encryptKey);
        String dncryptKey = AESUtils.decrypt(encryptKey);
        System.out.println("系统密钥解密:" + dncryptKey);





        // 明文
//        String value = "abc12345";
//        String encryptValue = AESUtils.encrypt(value);
//        System.out.println("加密:" + encryptValue);
//
//
//        System.out.println("-------------------");
//        String dncryptValue = AESUtils.decrypt(encryptValue);
//        System.out.println("解密:" + dncryptValue);
//        System.out.println(value.equals(dncryptValue));
    }




    public static String getKey(){
        String prefix = key[VERSION][0];
        String key1 = key[VERSION][1];
        String key2 = AESUtils.decrypt(key1);
        String key3 = key2.substring(0,LEN_16 + 1);
        String key4 = key2.substring(LEN_16 + 1);
        return prefix + "," + key3.substring(0,SPILT_INDEX) + key3.substring(SPILT_INDEX+1) + "," + key4.substring(0,SPILT_INDEX) + key4.substring(SPILT_INDEX+1);
    }


    private String generatorLeftKey() {
        String key = RandomStringUtils.randomAlphanumeric(LEN_16);
        return key.substring(0,SPILT_INDEX) + "V" + key.substring(SPILT_INDEX);
    }

    private String generatorRightKey()  {
        String key = RandomStringUtils.randomAlphanumeric(LEN_16);
        return key.substring(0,SPILT_INDEX) + "1" + key.substring(SPILT_INDEX);
    }




    private int getVersion(String prefix){
        return 0;
    }


    private String generatorKey() {
        String leftKey = generatorLeftKey();
        System.out.println("左:" + leftKey);
        String rightKey = generatorRightKey();
        System.out.println("右:" + rightKey);
        return leftKey + rightKey;
//        return leftKey.substring(0,SPILT_INDEX) + leftKey.substring(SPILT_INDEX+1) + rightKey.substring(0,SPILT_INDEX) + rightKey.substring(SPILT_INDEX+1);
    }

}
