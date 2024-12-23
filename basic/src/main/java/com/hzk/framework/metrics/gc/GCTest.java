package com.hzk.framework.metrics.gc;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.hzk.framework.metrics.CountTest;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 垃圾回收期
 */
public class GCTest {

    private static MetricRegistry REGISTRY = new MetricRegistry();

    static ConsoleReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .build();

    static {
        REGISTRY.register(CountTest.class.getName() + ".jvm.gc", new GarbageCollectorMetricSet());

        // 3秒定时输出
        REPORTER.start(3, TimeUnit.SECONDS);
    }

    @Test
    public void test1() throws Exception{
        System.in.read();
    }

}
