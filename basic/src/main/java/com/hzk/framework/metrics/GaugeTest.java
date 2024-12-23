package com.hzk.framework.metrics;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.DerivativeGauge;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;
import com.codahale.metrics.jmx.JmxReporter;
import com.codahale.metrics.jvm.JmxAttributeGauge;
import org.junit.Test;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Queue;
import java.util.SortedMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class GaugeTest {

    private static MetricRegistry REGISTRY = new MetricRegistry();

    static ConsoleReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY)
            .convertRatesTo(TimeUnit.SECONDS)
            .build();

    static {
        // 3秒定时输出
        REPORTER.start(3, TimeUnit.SECONDS);
    }

    @Test
    public void simpleGaugeTest() throws Exception{
        Queue<String> queue = new LinkedBlockingDeque<>();
        Gauge<Integer> gauge = () -> {
            return queue.size();
        };
        REGISTRY.register("simpleGaugeTest.gauge", gauge);
        while (true) {
            queue.add("a");
            Thread.currentThread().sleep(500);
        }
    }

    @Test
    public void jmxGaugeTest() throws Exception{
        System.err.println("进程ID:" + getProcessID());

        REGISTRY.register(MetricRegistry.name(GaugeTest.class, "HeapMemory"),
                new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));
        REGISTRY.register(MetricRegistry.name(GaugeTest.class, "NonHeapMemory"),
                new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "NonHeapMemory"));

        JmxReporter jmxReporter = JmxReporter.forRegistry(REGISTRY).build();
        jmxReporter.start();

        System.in.read();
    }

    private static int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);
    }

    @Test
    public void ratioGaugeTest() throws Exception{
        Meter totalMeter = new Meter();
        Meter successMeter = new Meter();

        REGISTRY.register("successRatio", new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                System.out.println("success:" + successMeter.getCount() + ",total:" + totalMeter.getCount());
                return Ratio.of(successMeter.getCount(), totalMeter.getCount());
            }
        });

        while (true){
            totalMeter.mark();
            try {
                int count = ThreadLocalRandom.current().nextInt(2);
                if (count == 0) {
                    System.out.println(">>>>>>>>>>>>>>>>>>");
                }
                int res = 3/count;
                successMeter.mark();
            } catch (Exception e) {

            }
            Thread.currentThread().sleep(500);
        }
    }

    @Test
    public void cachedGaugeTest() throws Exception{
        // 值缓存5秒，控制台reporter3秒获取值，缓存失效则会进loadValue
        REGISTRY.register("cachedGauge", new CachedGauge<Long>(5, TimeUnit.SECONDS) {
            @Override
            protected Long loadValue() {
                Long longValue = Long.valueOf(ThreadLocalRandom.current().nextInt(10_000));
                System.err.println("longValue:" + longValue);
                return longValue;
            }
        });

        new Thread(()->{
            SortedMap<String, Gauge> gaugeMap = REGISTRY.getGauges();
            Gauge cachedGauge = gaugeMap.get("cachedGauge");
            while (true) {
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 缓存失效则调用loadValue
                Object value = cachedGauge.getValue();
                System.out.println("线程A:" + value);
            }
        },"A").start();

        System.in.read();
    }

    /**
     * 自定义转换监控数据的监控类型DerivativeGauge
     * @throws Exception
     */
    @Test
    public void derivativeGaugeTest() throws Exception{
        AtomicLong atomicLong = new AtomicLong(0);
        Gauge<Long> guage = ()-> {
            return (long)atomicLong.get();
        };
        REGISTRY.register("cachedGauge", new DerivativeGauge<Long, String>(guage) {
            @Override
            protected String transform(Long value) {
                return String.format("第%s次调用", value);
            }
        });

        new Thread(()->{
            while (true) {
                atomicLong.incrementAndGet();
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
