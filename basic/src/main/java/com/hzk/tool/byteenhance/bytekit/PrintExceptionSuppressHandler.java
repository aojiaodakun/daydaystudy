package com.hzk.tool.byteenhance.bytekit;

import com.alibaba.bytekit.asm.binding.Binding;
import com.alibaba.bytekit.asm.interceptor.annotation.ExceptionHandler;

public class PrintExceptionSuppressHandler {

    @ExceptionHandler(inline = true)
    public static void onSuppress(@Binding.Throwable Throwable e, @Binding.Class Object clazz) {
        System.out.println("exception handler:" + clazz);
        e.printStackTrace();
    }

}
