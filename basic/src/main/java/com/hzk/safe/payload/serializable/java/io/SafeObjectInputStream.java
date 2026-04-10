package com.hzk.safe.payload.serializable.java.io;

import com.hzk.safe.payload.serializable.common.config.SerializationBlacklistConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class SafeObjectInputStream extends ObjectInputStream {

    public SafeObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc)
            throws IOException, ClassNotFoundException {
        String className = desc.getName();
        if (SerializationBlacklistConfig.isBlacklisted(className)) {
            throw new SecurityException("Blacklisted class detected: " + className);
        }
        return super.resolveClass(desc);
    }
}

