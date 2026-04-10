package com.hzk.webserver.filter.hessian;


import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.util.BitSet;

public class BitSetSerializer extends AbstractSerializer {
    private static BitSetSerializer SERIALIZER = new BitSetSerializer();

    public static BitSetSerializer create() {
        return SERIALIZER;
    }

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
        } else {
            BitSet bitSet = (BitSet) obj;
            out.writeObject(new BitSetHandle(bitSet.toLongArray()));
        }
    }

}
