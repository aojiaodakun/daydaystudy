package com.hzk.safe.payload.serializable.jackson;

public class Evil {

    public String msg;

    public Evil() {
        System.out.println(">>> Evil constructor called!");
    }

    @Override
    public String toString() {
        return "Evil{msg='" + msg + "'}";
    }

}
