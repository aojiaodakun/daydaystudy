package com.hzk.framework.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class HistogramTest {

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
        Histogram resultCounts = REGISTRY.histogram(HistogramTest.class.getName() + ".test1");
        new Thread(()->{
            int number = 0;
            while (true) {
                number++;
                resultCounts.update(number);
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
