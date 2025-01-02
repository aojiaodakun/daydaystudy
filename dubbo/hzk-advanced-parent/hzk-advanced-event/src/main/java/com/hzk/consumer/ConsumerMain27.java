package com.hzk.consumer;

import com.hzk.factory.ConsumerFactory;
import com.hzk.notify.INotify;
import com.hzk.notify.NotifyImpl;
import com.hzk.service.IDemoService;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;

import java.util.Arrays;

/**
 27、事件通知
 */
public class ConsumerMain27 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        /**
         * 27、事件通知
         */
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("sayHello");
        methodConfig.setAsync(false);
        INotify notify = new NotifyImpl();
        // 调用前
        methodConfig.setOninvoke(notify);
        methodConfig.setOninvokeMethod("oninvoke");
        // 调用后
        methodConfig.setOnreturn(notify);
        methodConfig.setOnreturnMethod("onreturn");
        // 报异常
        methodConfig.setOnthrow(notify);
        methodConfig.setOnthrowMethod("onthrow");
        referenceConfig.setMethods(Arrays.asList(methodConfig));
        // 服务引用
        IDemoService demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            String name = "hzk";
            System.out.println("remote invoke,param:" + name);
            demoService.sayHello(name);
            System.out.println("----------------------------");
            Thread.currentThread().sleep(1000*3);
        }

    }

}
