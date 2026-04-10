package com.hzk.java.oom.meta;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
/**
 * java.lang.OutOfMemoryError: Metaspace
 * 元空间OOM
 * -XX:MaxMetaspaceSize=20m -XX:+PrintGCDetails -XX:+PrintGCDateStamps
 * 元空间（Native Memory）用于存储类的元数据信息（如类名、方法信息、字节码等），通过动态生成大量类可以触发元空间OOM
 */
public class MetaspaceOOMTest1 {

    static class OOMObject {}

    public static void main(String[] args) {
        int classCounter = 0;

        try {
            while (true) {
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMObject.class);
                enhancer.setUseCache(false); // 禁用缓存确保每次生成新类
                enhancer.setCallback((MethodInterceptor) (obj, method, params, proxy) ->
                        proxy.invokeSuper(obj, params)
                );

                // 动态创建新类
                enhancer.create();

                // 每生成500个类打印一次计数
                if (++classCounter % 500 == 0) {
                    System.out.println("Generated classes: " + classCounter);
                }
            }
        } catch (Throwable t) {
            System.err.println("Metaspace OOM occurred after generating " + classCounter + " classes");
            t.printStackTrace();
        }
    }


}
