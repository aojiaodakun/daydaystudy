package com.hzk.safe.payload.serializable.hessian.bug;

import com.caucho.hessian.io.Hessian2Output;
import com.hzk.safe.payload.serializable.hessian.dto.ADTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * hessian序列化StackOverflowError
 * 原理：在Hessian序列化中，writeReplace方法是一个特殊的方法，它允许对象在序列化过程中指定一个替代对象来代表自己进行序列化。这个机制类似于Java原生序列化中的writeReplace方法
 * <dependency>
 *   <groupId>com.caucho</groupId>
 *   <artifactId>hessian</artifactId>
 *   <version>4.0.66</version>
 * </dependency>
 */
public class HessianStackOverFlowErrorTest {

    public static void main(String[] args) throws Exception {
        // 属性相互引用，不会java.lang.StackOverflowError
        try {
            byte[] serialize1 = serialize(new ADTO());
            System.out.println(1);
        } catch (Error e) {
            e.printStackTrace();
        }

        // 报错1
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            byte[] serialize1 = serialize(localDateTime);
        } catch (Error e) {
            e.printStackTrace();
        }
        System.out.println("-------------------------------");
        // 报错2
//        try {
//            StackOverFlowDTO stackOverFlowDTO = new StackOverFlowDTO();
//            byte[] serialize2 = serialize(stackOverFlowDTO);
//        } catch (Error e) {
//            e.printStackTrace();
//        }
        System.out.println("main end");
    }


    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        hessian2Output.writeObject(obj);
        hessian2Output.getBytesOutputStream().flush();
        hessian2Output.completeMessage();
        hessian2Output.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static class StackOverFlowDTO implements java.io.Serializable {
        public Object writeReplace(){
            return new MyReplacement(this);// 产生StackOverflowError
//            return new MyReplacement(null);// 无异常
        }
    }

    private static class MyReplacement implements java.io.Serializable {
        private StackOverFlowDTO stackOverFlowDTO;
        public MyReplacement(StackOverFlowDTO stackOverFlowDTO) {
            this.stackOverFlowDTO = stackOverFlowDTO;
        }
    }

}
