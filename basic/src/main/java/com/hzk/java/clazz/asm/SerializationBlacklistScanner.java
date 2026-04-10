package com.hzk.java.clazz.asm;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarFile;

public class SerializationBlacklistScanner {

    private static final List<String> BLACK_PACKAGE_PREFIX = Arrays.asList(
            ".*Transformer$",
            ".*InvocationHandler$",
            ".*Comparator$",
            ".*Factory$",
            ".*Remote.*",
            ".*Proxy$"
    );

    private static final List<String> BLACK_INTERFACE = Arrays.asList(
            "java.lang.reflect.InvocationHandler",
            "net.sf.cglib.proxy.Factory",
            "net.sf.cglib.proxy.MethodInterceptor",
            "org.springframework.cglib.proxy.Factory",
            "org.springframework.cglib.proxy.MethodInterceptor",
            "javassist.util.proxy.ProxyObject",
            "javassist.util.proxy.MethodHandler",
            "org.apache.ibatis.javassist.util.proxy.ProxyObject",
            "org.apache.ibatis.javassist.util.proxy.MethodHandler",
            "org.hibernate.proxy.HibernateProxy",
            "org.hibernate.collection.spi.PersistentCollection",
            "javax.naming.Reference",
            "javax.naming.spi.ObjectFactory",
            "java.rmi.Remote"
    );


    private static final String SERIALIZABLE = "java/io/Serializable";
    private static final String EXTERNALIZABLE = "java/io/Externalizable";

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\工作\\补丁\\bos_baseline\\mservice\\lib";
//        filePath = "D:\\工作\\补丁\\bos_baseline\\mservice\\lib\\trd\\fastjson-1.2.83.jar";

        Map<String, String> class2interfaceMap = new HashMap<>();
        Path path = Paths.get(filePath);
        if (Files.isDirectory(path)) {
            scanDirectory(path, class2interfaceMap);
        } else {
            scanJar(path, class2interfaceMap);
        }
        System.out.println("-------------");
        System.out.println(class2interfaceMap);
    }

    /* ========== 扫描目录 ========== */
    private static void scanDirectory(Path root, Map<String, String> class2interfaceMap) throws IOException {
        Files.walk(root)
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(jarPath -> {
                    try {
                        scanJar(jarPath, class2interfaceMap);
                    } catch (Exception e) {
                        System.err.println("scan jar failed: " + jarPath);
                        e.printStackTrace();
                    }
                });
    }


    /* ========== 扫描 JAR ========== */
    private static void scanJar(Path jarPath, Map<String, String> class2interfaceMap) throws IOException {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            jar.stream()
                    .filter(e -> e.getName().endsWith(".class"))
                    .forEach(e -> {
                        try {
                            byte[] bytes = IOUtils.toByteArray(jar.getInputStream(e));
                            scanClass(bytes, class2interfaceMap);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
        }
    }

    /* ========== 扫描单个 class ========== */
    private static void scanClass(byte[] bytecode, Map<String, String> class2interfaceMap) {
        ClassReader cr = new ClassReader(bytecode);

        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            String className;
            boolean serializable;
            boolean externalizable;

            @Override
            public void visit(
                    int version, int access, String name,
                    String signature, String superName, String[] interfaces) {

                className = name.replace('/', '.');

                if (interfaces != null) {
                    for (String itf : interfaces) {
                        if (SERIALIZABLE.equals(itf)) {
                            serializable = true;
                            for (String itf1 : interfaces) {
                                String tempitf = itf1.replace("/", ".");
                                if (BLACK_INTERFACE.contains(tempitf)) {
                                    class2interfaceMap.put(className, tempitf);
//                                    errorReport(className, tempitf, serializable, externalizable);
                                }
                            }
                        }
                        if (EXTERNALIZABLE.equals(itf)) {
                            externalizable = true;
                        }
                    }
                }
            }

            @Override
            public void visitEnd() {
                if (!serializable && !externalizable) {
                    return;
                }
                // 黑名单包模糊匹配
                for (String prefix : BLACK_PACKAGE_PREFIX) {
                    if (className.matches(prefix)) {
                        report(className, prefix, serializable, externalizable);
                        break;
                    }
                }
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
    }

    private static void report(String cls, String prefix,
                               boolean ser, boolean ext) {
        System.out.printf(
                "[MIS-BLOCK] class=%s prefix=%s Serializable=%s Externalizable=%s%n",
                cls, prefix, ser, ext
        );
    }

    private static void errorReport(String cls, String interfaceName,
                               boolean ser, boolean ext) {
        System.err.printf(
                "[MIS-INTERFACE] class=%s interface=%s Serializable=%s Externalizable=%s%n",
                cls, interfaceName, ser, ext
        );
    }
}

