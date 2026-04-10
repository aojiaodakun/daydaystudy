package com.hzk.webserver.filter.hessian;

import java.io.Serializable;

public class CommonRpcParam implements Serializable {

    private Object object;


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
