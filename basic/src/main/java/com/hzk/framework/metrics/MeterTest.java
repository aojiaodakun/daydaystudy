package com.hzk.framework.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class MeterTest {

    private static MetricRegistry REGISTRY = new MetricRegistry();

    static ConsoleReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .build();

    static {
        // 3秒定时输出
        REPORTER.start(3, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        String meterName = MetricRegistry.name(MeterTest.class, "request", "tps");
        Meter meterTps = new Meter();
        REGISTRY.register(meterName, meterTps);
        System.out.println("main");

        while (true) {
            meterTps.mark();
            Thread.currentThread().sleep(500);
        }

    }

}
