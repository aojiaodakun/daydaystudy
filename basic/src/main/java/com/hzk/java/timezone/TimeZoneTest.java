package com.hzk.java.timezone;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneTest {


    @Test
    public void test1() throws Exception{
        System.setProperty("user.timezone", "GMT+8");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(date);

        TimeZone tz1 = TimeZone.getDefault();
        df.setTimeZone(tz1);
        String strDate1 = df.format(date);
        System.out.println("默认时间："+strDate1);

        // 柏林
//        TimeZone tz2 = TimeZone.getTimeZone("Europe/Berlin");
//        df.setTimeZone(tz2);
//        String strDate2 = df.format(date);
//        System.out.println("柏林当前时间："+strDate2);



    }


    @Test
    public void BerlinTest() throws Exception{
//                TimeZone timeZone = TimeZone.getTimeZone("GMT+8");

        // 柏林时间,GMT+2
        String timeZoneString = "Europe/Berlin";
        // 东八区时间，上海,GMT+8
//        String timeZoneString = "Asia/Shanghai";
        System.setProperty("user.timezone", timeZoneString);
        TimeZone timeZone = TimeZone.getDefault();

        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(timeZone);
        String strDate1 = df.format(date);
        System.out.println("柏林当前时间："+strDate1);

        Date parse1 = df.parse(strDate1);
        System.out.println(parse1);
    }


    @Test
    public void test3() throws Exception{
        long dbTime = 1656486048000L;
        long selectTime = 1656507648000L;
        long newTime = selectTime - 6 * 60 * 60 * 1000;
        System.out.println(newTime);



    }


}
