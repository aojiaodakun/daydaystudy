package com.hzk.webserver.filter.hessian;

import com.caucho.hessian.io.*;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Instance-level Hessian SerializerFactory with blacklist & strict mode support.
 */
public class NativeHessianSerializerFactory extends SerializerFactory {

    public static final SerializerFactory SERIALIZER_FACTORY = new NativeHessianSerializerFactory(true);

    /* ===================== Instance Fields ===================== */

    /** 精确类名黑名单 */
    private final Set<String> blacklistClassSet = new CopyOnWriteArraySet<>();

    /** 包名前缀黑名单 */
    private final Set<String> blacklistPackageSet = new CopyOnWriteArraySet<>();

    /** 严格模式下的正则规则（类名匹配） */
    private final Set<String> strictModePatternSet = new CopyOnWriteArraySet<>();

    /** 是否启用严格模式 */
    private final boolean strictMode;

    /** 自定义序列化器 */
    private final Serializer bitSetSerializer = BitSetSerializer.create();

    /* ===================== Constructor ===================== */

    public NativeHessianSerializerFactory(boolean strictMode) {
        this.strictMode = strictMode;
        initDefaultRules();
    }

    /* ===================== Initialization ===================== */

    private void initDefaultRules() {
        initBlacklistClass();
        initBlacklistPackage();
        initStrictModePatterns();
    }

    private void initBlacklistClass() {
        addBlacklistClass("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        addBlacklistClass("org.apache.commons.collections.functors.InvokerTransformer");
        addBlacklistClass("org.apache.commons.collections.functors.InstantiateTransformer");
        addBlacklistClass("org.apache.commons.collections.functors.ChainedTransformer");
        addBlacklistClass("org.apache.commons.collections.functors.ConstantTransformer");
        addBlacklistClass("org.codehaus.groovy.runtime.ConvertedClosure");
        addBlacklistClass("org.codehaus.groovy.runtime.MethodClosure");
        addBlacklistClass("javax.management.remote.rmi.RMIConnector");
        addBlacklistClass("javax.naming.Reference");
        addBlacklistClass("com.sun.rowset.JdbcRowSetImpl");
        addBlacklistClass("java.lang.ProcessBuilder");
        addBlacklistClass("java.lang.Runtime");
    }

    private void initBlacklistPackage() {
        addBlacklistPackage("sun.rmi");
        addBlacklistPackage("com.sun");
        addBlacklistPackage("org.apache.xbean");
        addBlacklistPackage("org.apache.commons.beanutils");
        addBlacklistPackage("org.apache.commons.collections");
        addBlacklistPackage("org.springframework.beans.factory.config");
    }

    private void initStrictModePatterns() {
        addStrictModePattern(".*Transformer$");
        addStrictModePattern(".*InvocationHandler$");
        addStrictModePattern(".*Comparator$");
        addStrictModePattern(".*Factory$");
        addStrictModePattern(".*Jndi.*");
        addStrictModePattern(".*Remote.*");
        addStrictModePattern(".*Proxy$");
    }

    /* ===================== Public Configuration APIs ===================== */

    public void addBlacklistClass(String className) {
        if (className != null && !className.trim().isEmpty()) {
            blacklistClassSet.add(className.trim());
        }
    }

    public void addBlacklistPackage(String packagePrefix) {
        if (packagePrefix != null && !packagePrefix.trim().isEmpty()) {
            blacklistPackageSet.add(packagePrefix.trim());
        }
    }

    public void addStrictModePattern(String regex) {
        if (regex != null && !regex.trim().isEmpty()) {
            strictModePatternSet.add(regex.trim());
        }
    }

    /* ===================== Core Override ===================== */

    @Override
    public Deserializer getDeserializer(Class cl)
            throws HessianProtocolException {

        if (cl != null) {
            String className = cl.getName();

            if (isBlacklisted(className)) {
                throw new SecurityException(
                        "Hessian deserialization blocked (blacklist): " + className);
            }

            if (strictMode) {
                checkStrictMode(className);
            }
        }

        return super.getDeserializer(cl);
    }

    @Override
    public Serializer getSerializer(Class cl)
            throws HessianProtocolException {

        if (cl != null && java.util.BitSet.class.isAssignableFrom(cl)) {
            return bitSetSerializer;
        }
        return super.getSerializer(cl);
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /* ===================== Internal Logic ===================== */

    private boolean isBlacklisted(String className) {
        // 1. 精确类名
        if (blacklistClassSet.contains(className)) {
            return true;
        }

        // 2. 包名前缀
        for (String prefix : blacklistPackageSet) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }

        // 3. 内部类 → 外部类精确匹配
        int idx = className.indexOf('$');
        if (idx > 0) {
            String outerClass = className.substring(0, idx);
            return blacklistClassSet.contains(outerClass);
        }

        return false;
    }

    private void checkStrictMode(String className) {
        for (String regex : strictModePatternSet) {
            if (className.matches(regex)) {
                throw new SecurityException(
                        "Hessian deserialization blocked (strictMode): " + className);
            }
        }
    }
}
