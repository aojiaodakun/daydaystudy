package com.hzk.consumer;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.hzk.constants.HzkCommonConstants;
import com.hzk.proto.CDubboGooglePBRequestType;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;
/**
 * 18、GoogleProtobuf 对象泛化调用
 */
public class ConsumerMain18 {

    public static void main(String[] args) {
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        // 弱类型接口名
        referenceConfig.setInterface("com.hzk.service.IDemoService");
        // 声明为Protobuf-json
//        referenceConfig.setGeneric(true);
        referenceConfig.setGeneric(CommonConstants.GENERIC_SERIALIZATION_PROTOBUF);
        // 应用配置
        referenceConfig.setApplication(new ApplicationConfig(HzkCommonConstants.APPLICATION_NAME_CONSUMER));
        // 注册配置
        referenceConfig.setRegistry(new RegistryConfig(HzkCommonConstants.REGISTRY_ZK_2181));

        GenericService genericService = referenceConfig.get();


        invoke1(genericService);
        invoke2(genericService);
    }

    private static void invoke1(GenericService genericService) {
        Map<String, Object> person = new HashMap<>();
        person.put("double", "1.000");
        person.put("float", "1.00");
        person.put("int32","1" );
        person.put("bool","false" );
        //String 的对象需要经过base64编码
        person.put("string","someBaseString");
        person.put("bytesType","150");
        // 参考google官方的protobuf 3 的语法，服务的每个方法中只传输一个POJO对象
        // protobuf的泛化调用只允许传递一个类型为String的json对象来代表请求参数
        String requestString = new Gson().toJson(person);
        // 返回对象是GoolgeProtobuf响应对象的json字符串。
        Object result = genericService.$invoke("sayHello", new String[]{
                        "com.hzk.proto.CDubboGooglePBRequestType"},
                new Object[]{requestString});
        System.out.println(result);
    }


    private static void invoke2(GenericService genericService){
        CDubboGooglePBRequestType requestType = CDubboGooglePBRequestType.newBuilder()
                .setDouble(1.000)
                .setFloat(1.00f)
                .setInt32(1)
                .setBool(false)
                .setString("someBaseString")
                .setBytesType(ByteString.copyFrom("150".getBytes()))
                .build();
        String requestString = new Gson().toJson(requestType);
        // 返回对象是GoolgeProtobuf响应对象的json字符串。
        Object result = genericService.$invoke("sayHello", new String[]{
                        "com.hzk.proto.CDubboGooglePBRequestType"},
                new Object[]{requestString});
        System.out.println(result);
    }

}
