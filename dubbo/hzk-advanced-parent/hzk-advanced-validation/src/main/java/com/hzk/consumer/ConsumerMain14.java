package com.hzk.consumer;

import com.hzk.constants.HzkCommonConstants;
import com.hzk.dto.ParamValidationDTO;
import com.hzk.factory.ConsumerFactory;
import com.hzk.service.IDemoService;
import com.hzk.service.IDemoService14;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

/**
 14、参数验证
 */
public class ConsumerMain14 {

    public static void main(String[] args) throws Exception{
        ReferenceConfig<IDemoService14> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(IDemoService14.class);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));

        /**
         14、参数验证
         */
        referenceConfig.setValidation("true");

        ParamValidationDTO paramDTO = new ParamValidationDTO();
        paramDTO.setName("hzk");
        paramDTO.setAge(17);

        // 服务引用
        IDemoService14 demoService = referenceConfig.get();
        System.out.println("服务引用完成");
        // 服务调用
        while (true) {
            String name = "hzk";
            System.out.println("remote invoke,param:" + name);
            String result = demoService.paramValid(paramDTO);
            System.out.println(result);
            Thread.currentThread().sleep(1000*3);
        }

    }

}
