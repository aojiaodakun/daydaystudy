package com.hzk.safe.payload.serializable.hessian.mq;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianMessageSerde {

    public static byte[] encode(Message message) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output h2o = new Hessian2Output(os);
        try {
            h2o.startMessage();
            h2o.writeObject(message);
            h2o.completeMessage();
            h2o.close();

            byte[] buffer = os.toByteArray();
            os.close();
            return buffer;
        } catch (Exception e) {
            throw new IllegalArgumentException("can't encode message.", e);
        }
    }

    public static Message decode(byte[] bytes) {
        Hessian2Input h2i = new Hessian2Input(new ByteArrayInputStream(bytes));
        try {
            h2i.startMessage();
            return (Message) h2i.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("can't decode message.", e);
        }
    }

}
