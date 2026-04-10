package com.hzk.safe.payload.serializable.hessian.factory;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.hzk.safe.payload.serializable.common.config.SerializationBlacklistConfig;

public class NativeHessianSerializerFactory extends SerializerFactory {

    public static final SerializerFactory SERIALIZER_FACTORY = new NativeHessianSerializerFactory();

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        // 检查黑名单
        if (cl != null && SerializationBlacklistConfig.isBlacklisted(cl)) {
            throw new SecurityException("Blacklisted class detected: " + cl.getName());
        }
        return super.getSerializer(cl);
    }


    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        // 检查黑名单
        if (cl != null && SerializationBlacklistConfig.isBlacklisted(cl)) {
            throw new SecurityException("Blacklisted class detected: " + cl.getName());
        }
        return super.getDeserializer(cl);
    }

}

