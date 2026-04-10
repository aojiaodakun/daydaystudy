package com.hzk.safe.payload.serializable.hessian.mq;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.CollectionDeserializer;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

import java.util.List;

public class MQHessianTest {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            SerializerFactory factory = SerializerFactory.createDefault();
            factory.addFactory(new KRPCSerializerFactory());
        }

        while (true) {
            Thread.currentThread().sleep(1000 * 1);
        }



//        Map<String, Object> map = new HashMap<>();
//        map.put("message", "body");
//        map.put("message1", null);
//        Message message = new Message();
//        message.setBody(map);
//        byte[] bytes = HessianMessageSerde.encode(message);
//        System.out.println("--------------------------");
//        Message decodeMessage = HessianMessageSerde.decode(bytes);
//        System.out.println(decodeMessage);
    }

    public static class KRPCSerializerFactory extends AbstractSerializerFactory {
        public KRPCSerializerFactory() {
        }

        public Serializer getSerializer(Class cl) throws HessianProtocolException {
            return null;
        }

        public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
            return cl.equals(Iterable.class) ? new CollectionDeserializer(List.class) : null;
        }
    }


}
