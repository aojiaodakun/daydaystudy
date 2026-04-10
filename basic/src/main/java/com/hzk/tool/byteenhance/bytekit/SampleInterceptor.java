package com.hzk.tool.byteenhance.bytekit;

import com.alibaba.bytekit.asm.binding.Binding;
import com.alibaba.bytekit.asm.interceptor.annotation.AtEnter;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExceptionExit;
import com.alibaba.bytekit.asm.interceptor.annotation.AtExit;

public class SampleInterceptor {

    @AtEnter(inline = false, suppress = RuntimeException.class, suppressHandler = PrintExceptionSuppressHandler.class)
    public static void atEnter(@Binding.This Object object,
                               @Binding.Class Object clazz,
                               @Binding.Args Object[] args,
                               @Binding.MethodName String methodName,
                               @Binding.MethodDesc String methodDesc) {
        System.out.println("atEnter, args[0]:" + args[0]);
    }

    @AtExit(inline = true)
    public static void atExit(@Binding.Return Object returnObject) {
        System.out.println("atExit, returnObject:" + returnObject);
    }

    @AtExceptionExit(inline = true, onException = RuntimeException.class)
    public static void atExceptionExit(@Binding.Throwable RuntimeException ex, @Binding.Field(name = "exceptionCount") int exceptionCount) {
        System.out.println("atExceptionExit, ex:" + ex.getMessage() + ",field exceptionCount:" + exceptionCount);
    }

}
