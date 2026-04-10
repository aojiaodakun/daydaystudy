package com.hzk.tool.byteenhance.bytekit;

import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
import com.alibaba.bytekit.utils.AgentUtils;
import com.alibaba.bytekit.utils.AsmUtils;
import com.alibaba.deps.org.objectweb.asm.tree.ClassNode;
import com.alibaba.deps.org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

public class EnhanceUtil {

    public static byte[] enhanceClass(Class targetClass, String[] targetMethodNames, Class interceptorClass) throws Exception{
        AgentUtils.install();

        DefaultInterceptorClassParser interceptorClassParser = new DefaultInterceptorClassParser();
        List<InterceptorProcessor> processorList = interceptorClassParser.parse(interceptorClass);

        ClassNode classNode = AsmUtils.loadClass(targetClass);

        List<String> methodNameList = Arrays.asList(targetMethodNames);

        for(MethodNode methodNode : classNode.methods) {
            if (methodNameList.contains(methodNode.name)) {
                MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode);
                for (InterceptorProcessor interceptor : processorList) {
                    interceptor.process(methodProcessor);
                }
            }
        }

        return AsmUtils.toBytes(classNode);
    }

}
