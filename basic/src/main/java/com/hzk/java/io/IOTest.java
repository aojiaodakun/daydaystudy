package com.hzk.java.io;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class IOTest implements Serializable {

    public static void main(String[] args) throws Exception {
        IOTest ioTest = new IOTest();
        byte[] bytes = toByte(ioTest);
        System.out.println(bytes);
    }

    private static byte[] toByte(Object o) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            byte[] bytes = baos.toByteArray();
            oos.close();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
