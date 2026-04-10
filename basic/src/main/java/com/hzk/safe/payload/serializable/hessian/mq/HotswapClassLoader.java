//package com.hzk.safe.payload.serializable.hessian.mq;
//
//import java.io.Serializable;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.HashMap;
//import java.util.Map;
//
//// 模拟热部署的类加载器
//public class HotswapClassLoader extends URLClassLoader {
//    public HotswapClassLoader(URL[] urls, ClassLoader parent) {
//        super(urls, parent);
//    }
//
//    public Class<?> reloadClass(String name, byte[] bytecode) {
//        definePackage(name);
//        return defineClass(name, bytecode, 0, bytecode.length);
//    }
//}
//
//
