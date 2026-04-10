package com.hzk.webserver.filter.hessian;

import com.caucho.hessian.io.HessianHandle;

import java.io.Serializable;
import java.util.BitSet;

public class BitSetHandle implements Serializable, HessianHandle {
    private static final long serialVersionUID = -8758682133916767946L;
    private long[] value;

    public BitSetHandle(long[] bitSetValue) {
        this.value = bitSetValue;
    }

    private Object readResolve() {
        return BitSet.valueOf(this.value);
    }
}
