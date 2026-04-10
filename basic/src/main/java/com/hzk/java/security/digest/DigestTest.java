package com.hzk.java.security.digest;

public class DigestTest {

    public static void main(String[] args) throws Exception {
        String md5 = DigestUtil.md5("hello world");
        System.out.println(md5);
    }

}
