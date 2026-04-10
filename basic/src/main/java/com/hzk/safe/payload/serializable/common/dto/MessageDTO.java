package com.hzk.safe.payload.serializable.common.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {

    private String msg;
    private RequestContextDTO requestContext;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RequestContextDTO getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(RequestContextDTO requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "msg='" + msg + '\'' +
                ", requestContext=" + requestContext +
                '}';
    }
}
