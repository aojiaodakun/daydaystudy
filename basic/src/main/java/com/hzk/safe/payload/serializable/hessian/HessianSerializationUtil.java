package com.hzk.safe.payload.serializable.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.hzk.safe.payload.serializable.hessian.factory.NativeHessianSerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class HessianSerializationUtil {

    public static byte[] serialize(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Hessian2Output h2o = new Hessian2Output(bos);
            h2o.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            h2o.writeObject(object);
            h2o.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object deserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            Hessian2Input h2i = new Hessian2Input(bis);
            h2i.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            return h2i.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] serializeMessage(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Hessian2Output h2o = new Hessian2Output(bos);
            h2o.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            h2o.startMessage();
            h2o.writeObject(object);
            h2o.completeMessage();
            h2o.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("serializeMessage error.", e);
        }
    }

    public static Object deserializeMessage(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            Hessian2Input h2i = new Hessian2Input(bis);
            h2i.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            h2i.startMessage();
            return h2i.readObject();
        } catch (IOException e) {
            throw new RuntimeException("deserializeMessage error.", e);
        }
    }


    public static byte[] gzipSerialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
            Hessian2Output h2o = new Hessian2Output(gos);
            h2o.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            h2o.writeObject(object);
            h2o.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }


    public static Object gzipDeserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);GZIPInputStream gis = new GZIPInputStream(bis)) {
            Hessian2Input h2i = new Hessian2Input(gis);
            h2i.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            return h2i.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
