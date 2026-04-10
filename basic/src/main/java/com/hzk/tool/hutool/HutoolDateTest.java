package com.hzk.tool.hutool;

import cn.hutool.core.date.DateTime;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * hutoolDateTime序列化问题
 * <dependency>
 * <groupId>cn.hutool</groupId>
 * <artifactId>hutool-all</artifactId>
 * <version>5.8.31</version>
 * </dependency>
 * <dependency>
 * <groupId>com.caucho</groupId>
 * <artifactId>hessian</artifactId>
 * <version>4.0.66</version>
 * </dependency>
 */
public class HutoolDateTest {

    public static void main(String[] args) throws Exception {
        cn.hutool.core.date.DateTime today =
                cn.hutool.core.date.DateUtil.parse(cn.hutool.core.date.DateUtil.today());// 反序列化1970-01-01 08:00:00
//        java.util.Date today = new Date();// 反序列化正常
        System.out.println("原:" + today);
        byte[] bytes = serialize(today);
        Date newToday = (Date) deserialize(bytes);
        System.out.println("反序列化:" + newToday);
    }

    // 序列化
    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        hessian2Output.writeObject(obj);
        hessian2Output.getBytesOutputStream().flush();
        hessian2Output.completeMessage();
        hessian2Output.close();
        return byteArrayOutputStream.toByteArray();
    }

    // 反序列化
    private static Object deserialize(byte[] bytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            Hessian2Input input = new Hessian2Input(inputStream);
            Object deserializeObject = input.readObject();
            input.close();
            return deserializeObject;
        }
    }

}
