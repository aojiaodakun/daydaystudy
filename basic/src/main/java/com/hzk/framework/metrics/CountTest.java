package com.hzk.framework.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CountTest {


    private static MetricRegistry REGISTRY = new MetricRegistry();

    static ConsoleReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .build();

    static {
        // 3秒定时输出
        REPORTER.start(3, TimeUnit.SECONDS);
    }

    @Test
    public void test1() throws Exception{
        Counter counter = new Counter();
        REGISTRY.register(CountTest.class.getName() + ".test1", counter);

        new Thread(()->{
            while (true) {
                counter.inc();
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        System.in.read();
    }


}
