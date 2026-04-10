package com.hzk.tool.hutool;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;

public class HutoolSM4Test {

    public static void main(String[] args) throws Exception {
        byte[] encoded = SmUtil.sm4().getSecretKey().getEncoded();
        SM4 sm4 = SmUtil.sm4(encoded);
        String str = "abc";
        byte[] encryptBytes = sm4.encrypt(str);
        byte[] decryptBytes = sm4.decrypt(encryptBytes);
        String newStr = new String(decryptBytes);
        boolean flag = str.equals(newStr);
        System.out.println("加解密对称:" + flag);
    }

}
