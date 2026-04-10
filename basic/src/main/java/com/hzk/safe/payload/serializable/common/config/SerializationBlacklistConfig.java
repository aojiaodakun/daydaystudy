package com.hzk.safe.payload.serializable.common.config;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 序列化黑名单配置
 */
public class SerializationBlacklistConfig {


    // 黑名单类名集合
    private static final Set<String> BLACK_LIST_CLASS_SET = new CopyOnWriteArraySet<>();
    // 黑名单包名前缀集合
    private static final Set<String> BLACK_LIST_PACKAGE_SET = new CopyOnWriteArraySet<>();
    // 严格模式-黑名单模糊类名
    private static final Set<String> BLACK_LIST_STRICTMODE_CLASS_SET = new CopyOnWriteArraySet<>();
    // 是否启用严格模式
    private static volatile boolean strictMode = false;

    static {
        initDefaultBlacklist();
    }

    /**
     * 初始化默认黑名单
     */
    private static void initDefaultBlacklist() {
        // 黑名单类
        initBlacklistClass();
        // 黑名单包
        initBlacklistPackage();
        // 严格模式的类名、接口名检查
        initBlacklistStrictModeClass();
    }

    private static void initBlacklistClass() {
        // 1. 已知的危险类
        addBlacklistClass("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        addBlacklistClass("org.apache.commons.collections.functors.InvokerTransformer");
        addBlacklistClass("org.apache.commons.collections.functors.InstantiateTransformer");
        addBlacklistClass("org.apache.commons.collections.functors.ChainedTransformer");
        addBlacklistClass("org.apache.commons.collections.functors.ConstantTransformer");
        addBlacklistClass("org.codehaus.groovy.runtime.ConvertedClosure");
        addBlacklistClass("org.codehaus.groovy.runtime.MethodClosure");
        addBlacklistClass("javax.swing.UIDefaults");
        addBlacklistClass("javax.swing.UIDefaults$ProxyLazyValue");

        // TODO
        addBlacklistClass("org.codehaus.groovy.runtime.ConvertedClosure");

        // 2. JNDI注入相关
        addBlacklistClass("javax.management.remote.rmi.RMIConnector");
        addBlacklistClass("javax.naming.Reference");
        addBlacklistClass("com.sun.rowset.JdbcRowSetImpl");

        // 3. 文件操作相关
        addBlacklistClass("java.io.FileInputStream");
        addBlacklistClass("java.io.FileOutputStream");

        // 4. 进程执行相关
        addBlacklistClass("java.lang.ProcessBuilder");
        addBlacklistClass("java.lang.Runtime");
    }

    private static void initBlacklistPackage() {
        addBlacklistPackage("sun.rmi");
        addBlacklistPackage("com.sun");
        addBlacklistPackage("org.apache.xbean");
        addBlacklistPackage("org.apache.commons.beanutils");
        addBlacklistPackage("org.apache.commons.collections");
        addBlacklistPackage("org.springframework.beans.factory.config");

        // TODO
        addBlacklistPackage("org.springframework.aop");
        addBlacklistPackage("org.springframework.cache.interceptor");
    }

    private static void initBlacklistStrictModeClass() {
        addBlacklistStrictModeClass(".*Transformer$");
        addBlacklistStrictModeClass(".*InvocationHandler$");
        addBlacklistStrictModeClass(".*Comparator$");
        addBlacklistStrictModeClass(".*Factory$");
        addBlacklistStrictModeClass(".*Jndi.*");
        addBlacklistStrictModeClass(".*Remote.*");
        addBlacklistStrictModeClass(".*Proxy$");
    }

    /**
     * 添加黑名单类
     * @param className 全类名
     */
    static void addBlacklistClass(String className) {
        if (className != null && !className.trim().isEmpty()) {
            BLACK_LIST_CLASS_SET.add(className.trim());
        }
    }

    /**
     * 添加黑名单包名前缀
     * @param packagePrefix 包名前缀
     */
    static void addBlacklistPackage(String packagePrefix) {
        if (packagePrefix != null && !packagePrefix.trim().isEmpty()) {
            BLACK_LIST_PACKAGE_SET.add(packagePrefix.trim());
        }
    }

    /**
     * 严格模式添加黑名单模糊类名
     * @param className 模糊类名
     */
    static void addBlacklistStrictModeClass(String className) {
        if (className != null && !className.trim().isEmpty()) {
            BLACK_LIST_STRICTMODE_CLASS_SET.add(className.trim());
        }
    }

    /**
     * 检查是否在黑名单中
     */
    public static boolean isBlacklisted(String className) {
        // 1. 检查完整类名
        if (BLACK_LIST_CLASS_SET.contains(className)) {
            return true;
        }

        // 2. 检查包名前缀
        for (String packagePrefix : BLACK_LIST_PACKAGE_SET) {
            if (className.startsWith(packagePrefix)) {
                return true;
            }
        }

        // 3. 检查内部类
        if (className.contains("$")) {
            String outerClassName = className.substring(0, className.indexOf('$'));
            if (BLACK_LIST_CLASS_SET.contains(outerClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否在黑名单中
     */
    public static boolean isBlacklisted(Class<?> clazz) {
        String className = clazz.getName();
        // 1. 检查完整类名
        if (BLACK_LIST_CLASS_SET.contains(className)) {
            return true;
        }

        // 2. 检查包名前缀
        for (String packagePrefix : BLACK_LIST_PACKAGE_SET) {
            if (className.startsWith(packagePrefix)) {
                return true;
            }
        }

        // 3. 检查内部类
        if (className.contains("$")) {
            String outerClassName = className.substring(0, className.indexOf('$'));
            if (BLACK_LIST_CLASS_SET.contains(outerClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 严格的类安全检查
     */
    private void checkClassStrictMode(Class cl) {
        String className = cl.getName();
        for (String pattern : BLACK_LIST_STRICTMODE_CLASS_SET) {
            if (className.matches(pattern)) {
                throw new SecurityException("Potentially dangerous class detected: " + className);
            }
        }
    }

}
