package com.hzk.java.clazz;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * AppClassLoader可动态加载磁盘的class文件并执行
 */
public class ClassLoaderAddPathTest {

    public static void main(String[] args) throws Exception {
//        String classesPath = "D:/project/daydaystudy/basic/target/classes";
        String classesPath = "D:\\temp\\classes";
        String className = "com.hzk.test.BasicTest";
        String methodName = "test1";
        addPathAndExec(classesPath, className, methodName, args);
    }

    private static void addPathAndExec(String classesPath, String className, String methodName, String[] args) throws Exception{
        File classesPathFile = new File(classesPath);
        if (!classesPathFile.exists()) {
            System.err.println("File not found: " + classesPath + "\n");
            return;
        }
        // 1、创建className的包名，copy文件到指定包名下
        copyClass(classesPath, className);
        // 2、将class所在目录加入AppClassLoader
        addPathToClassLoader(classesPathFile);
        // 3、使用AppClassLoader加载类
        Class<?> clazz = Class.forName(className, true, ClassLoader.getSystemClassLoader());
        // 4、反射调用方法
        Method method = clazz.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(clazz.newInstance(), null);
    }

    private static void copyClass(String classesPath, String className) throws IOException {
        String[] classNameArray = className.split("\\.");
        String simpleClassName = classNameArray[classNameArray.length - 1];
        String packageName = className.replace( "." + simpleClassName, "");
        // 将包名中的点替换为文件分隔符
        String packagePath = packageName.replace('.', File.separatorChar);
        File originClassFile = new File(classesPath + "/" + simpleClassName + ".class");
        // 拼接完整路径
        Path targetPath = Paths.get(classesPath, packagePath, simpleClassName + ".class");
        copyFileUsingNIO(originClassFile.getAbsolutePath(), targetPath.toFile().getAbsolutePath());
    }

    // 动态添加路径到AppClassLoader
    private static void addPathToClassLoader(File dir) throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (cl instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) cl;
            Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);
            URL url = dir.toURI().toURL();
            addUrlMethod.invoke(urlClassLoader, url);
        } else {
            throw new UnsupportedOperationException("AppClassLoader is not URLClassLoader (Java 9+?)");
        }
    }

    public static void copyFileUsingNIO(String sourcePath, String destPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destPath);
        // 如果目标目录不存在则创建
        Files.createDirectories(destination.getParent());
        // 拷贝文件，如果目标文件已存在则替换
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("文件拷贝成功: " + source + " -> " + destination);
    }

    // 从class文件解析类名（需实现正确解析逻辑）
    private static String parseClassName(String execClassPath) throws IOException {
        // NIO
        try (InputStream in = Files.newInputStream(Paths.get(new File(execClassPath).getAbsolutePath()))) {
            ClassReader reader = new ClassReader(in);
            return reader.getClassName().replace('/', '.');
        }
        // IO
//        try (InputStream in = new FileInputStream(new File(execClassPath))) {
//            ClassReader reader = new ClassReader(in);
//            return reader.getClassName().replace('/', '.');
//        }

    }

}
