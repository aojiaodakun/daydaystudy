package com.hzk.framework.metrics.reporter;

import com.codahale.metrics.Counter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.hzk.framework.metrics.CountTest;
import org.junit.Test;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReporterTest {

    private static MetricRegistry REGISTRY = new MetricRegistry();

    static File FILE = new File("D:\\workspace\\IDEA\\basic\\src\\main\\java\\com\\hzk\\metrics\\reporter\\data");

    static CsvReporter CSVREPORTER = CsvReporter.forRegistry(REGISTRY)
            .formatFor(Locale.US)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build(FILE);

    static {
        // 3秒定时输出
        CSVREPORTER.start(3, TimeUnit.SECONDS);
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
