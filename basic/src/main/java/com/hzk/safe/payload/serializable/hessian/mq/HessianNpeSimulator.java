package com.hzk.safe.payload.serializable.hessian.mq;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class HessianNpeSimulator {

    static class ProblemObject implements java.io.Serializable {
        // 包含可能为null的Map字段
        private Map<String, Object> problemMap;

        public ProblemObject() {
            // 故意不初始化Map
        }
    }

    public static void main(String[] args) {
        try {
            // 1. 创建问题对象（包含未初始化的Map字段）
            ProblemObject obj = new ProblemObject();

            // 2. 创建Hessian序列化环境
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Hessian2Output out = new Hessian2Output(bos);

            // 3. 使用默认序列化工厂（不添加安全处理）
            SerializerFactory factory = new SerializerFactory();
            out.setSerializerFactory(factory);

            System.out.println("Starting serialization...");

            // 4. 触发序列化（将抛出NPE）
            out.writeObject(obj);
            out.close();

            System.out.println("Serialization completed successfully");
        } catch (Exception e) {
            System.err.println("Exception occurred:");
            e.printStackTrace();

            // 验证是否为预期的异常堆栈
            boolean isExpected = false;
            for (StackTraceElement ste : e.getStackTrace()) {
                if (ste.getClassName().equals("com.caucho.hessian.io.SerializerFactory") &&
                        ste.getMethodName().equals("loadSerializer")) {
                    isExpected = true;
                    break;
                }
            }

            if (isExpected) {
                System.out.println("\n=== Successfully reproduced the target NPE ===");
            }
        }
    }
}
