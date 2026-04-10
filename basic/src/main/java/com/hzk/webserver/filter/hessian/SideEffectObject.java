package com.hzk.webserver.filter.hessian;

import java.io.Serializable;

public class SideEffectObject implements Serializable {

    static {
        System.out.println("### static block executed ###");
    }

    private void readObject(java.io.ObjectInputStream in)
            throws Exception {
        in.defaultReadObject();
        System.out.println("### readObject executed ###");
    }

}
