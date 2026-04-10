//package com.hzk.safe.payload.serializable.hessian.mq;
//
//import com.caucho.hessian.io.Hessian2Output;
//import com.caucho.hessian.io.SerializerFactory;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.Serializable;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//public class HotDeployNpeSimulator {
//    public static void main(String[] args) throws Exception {
//        // 初始加载
//        byte[] classBytes = loadClassBytes("Service");
//        HotswapClassLoader loader1 = createLoader();
//        Class<?> serviceClassV1 = loader1.reloadClass("Service", classBytes);
//        Object serviceV1 = serviceClassV1.newInstance();
//
//        // 初始序列化（填充缓存）
//        hessianSerialize(serviceV1);
//
//        // 模拟热部署 - 使用新类加载器加载相同类
//        HotswapClassLoader loader2 = createLoader();
//        Class<?> serviceClassV2 = loader2.reloadClass("Service", classBytes);
//
//        // 关键：尝试序列化旧版本对象
//        hessianSerialize(serviceV1); // 这里触发NPE
//    }
//
//    private static void hessianSerialize(Object obj) throws IOException {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        Hessian2Output out = new Hessian2Output(bos);
//        out.setSerializerFactory(new SerializerFactory());
//        out.writeObject(obj);
//        out.close();
//    }
//
//    private static HotswapClassLoader createLoader() {
//        return new HotswapClassLoader(new URL[0],
//                Thread.currentThread().getContextClassLoader());
//    }
//
//    // 测试类
//    private static class Service implements Serializable {
//        private Map<String, Object> data; // 可序列化字段
//
//        public Service() {
//            data = new HashMap<>();
//            data.put("key", "value");
//        }
//    }
//
//}
