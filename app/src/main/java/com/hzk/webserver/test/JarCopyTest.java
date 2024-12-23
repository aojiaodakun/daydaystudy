package com.hzk.webserver.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 将bos-resources.jar/webapp/web.xml拷贝到指定路径
 */
public class JarCopyTest {

    public static void main(String[] args) throws Exception{
        String jarPath = "file:/D:/project/bes-test/src/main/resources/lib/bos-resources.jar!/webapp/web.xml";
        String jarProtocol = "jar:";
        String jarXmlPath = jarProtocol + jarPath;
        File file = new File(jarXmlPath);
        boolean exists = file.exists();
        System.out.println(exists);

        // web.xml源
        URI uri = URI.create(jarXmlPath);
        URL url = uri.toURL();
        InputStream inputStream = url.openConnection().getInputStream();

        URL resource = String.class.getResource("/");

//        FileOutputStream fileOutputStream = new FileOutputStream("D:\\project\\bes-test\\src\\main\\resources\\webapp\\web.xml");
        FileOutputStream fileOutputStream = new FileOutputStream(String.class.getResource("/").getPath() +
                "webapp/web.xml");
        byte[] bytes = new byte[1024];
        int read = 0;
        while ((read = inputStream.read(bytes)) != -1){
            fileOutputStream.write(bytes, 0, read);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }

}
