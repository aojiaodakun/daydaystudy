package com.hzk.tool.byteenhance.bytekit;

import com.alibaba.bytekit.utils.AgentUtils;
import com.alibaba.bytekit.utils.Decompiler;

/**
 * https://blog.csdn.net/hengyunabc/article/details/107398983
 */
public class BytekitTest {

    public static void main(String[] args) throws Exception{
        try {
            Sample sample = new Sample();
            sample.hello("1", false);
            sample.hello("2", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        // class增强
        byte[] bytes = EnhanceUtil.enhanceClass(Sample.class, new String[]{"hello"}, SampleInterceptor.class);
        System.out.println(Decompiler.decompile(bytes));

        AgentUtils.reTransform(Sample.class, bytes);

        System.out.println("------------after reTransform--------");
        try {
            Sample sample = new Sample();
            sample.hello("3", false);
            sample.hello("4", true);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

    }

}
