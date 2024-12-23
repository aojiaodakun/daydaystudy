package com.hzk.framework.metrics.memory;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.hzk.framework.metrics.CountTest;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 堆内存
 */
public class MemoryTest {

    private static MetricRegistry REGISTRY = new MetricRegistry();

    static ConsoleReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .build();

    static {
        REGISTRY.register(CountTest.class.getName() + ".jvm.memory", new MemoryUsageGaugeSet());

        // 3秒定时输出
        REPORTER.start(3, TimeUnit.SECONDS);
    }

    @Test
    public void test1() throws Exception{
        System.in.read();
    }

}
