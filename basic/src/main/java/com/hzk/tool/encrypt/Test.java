package com.hzk.tool.encrypt;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
        // 精度丢失
        BigDecimal bigDecimal = new BigDecimal(Double.toString(1111911111911.1239999998d));
        System.out.println(bigDecimal.toString());
        System.out.println(bigDecimal.toPlainString());
        System.out.println("-----------------------");
        // 正常
        String str = "1111911111911.1239999998";
        BigDecimal bigDecimal1 = new BigDecimal(str);
        System.out.println(bigDecimal1.toString());
        System.out.println(bigDecimal1.toPlainString());
    }

}
