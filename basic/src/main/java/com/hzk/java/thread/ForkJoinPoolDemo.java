package com.hzk.java.thread;

import com.hzk.java.thread.service.Calculator;
import com.hzk.java.thread.service.impl.ForkJoinCalculator;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

public class ForkJoinPoolDemo {

    public static void main(String[] args) {
        long[] numbers = LongStream.rangeClosed(1, 10000000).toArray();
        Instant start = Instant.now();
        Calculator calculator = new ForkJoinCalculator();
        long result = calculator.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result);
    }



    public static void method2(){
        Instant start = Instant.now();
        long result = LongStream.rangeClosed(0, 10000000L).parallel().reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result); // 打印结果500500
    }

}
