package com.hzk.tool.byteenhance.bytekit;

public class Sample {

    private int exceptionCount = 0;

    public String hello(String str, boolean exception) {
        if (exception) {
            exceptionCount++;
            throw new RuntimeException("test exception, str:" + str);
        }
        return "hello " + str;
    }

}
