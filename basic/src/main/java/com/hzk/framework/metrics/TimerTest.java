package com.hzk.framework.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TimerTest {

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
        Timer timer = REGISTRY.timer(this.getClass().getName() + ".test1");

        new Thread(()->{
            Timer.Context context = timer.time();
            while (true) {
                try {
                    System.out.println("execute task");
                    context.stop();
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        System.in.read();
    }
}
