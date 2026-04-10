package com.hzk.framework.prometheus;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

public class PrometheusDemo {

    // Gauge 示例（可设置任意值）
    private static final Gauge activeUsers = Gauge.build()
            .name("demo_active_users")
            .help("Number of active users")
            .labelNames("region")
            .register();

    // Counter 示例（只能递增）
    private static final Counter requestCounter = Counter.build()
            .name("demo_request_total")
            .help("Total request count")
            .labelNames("method")
            .register();

    // Histogram 示例（统计耗时）
    private static final Histogram requestLatency = Histogram.build()
            .name("demo_request_latency_seconds")
            .help("Request latency in seconds")
            .buckets(0.005, 0.01, 0.05, 0.1, 0.5, 1, 5)
            .register();

    public static void main(String[] args) throws Exception {
        // 默认 JVM metrics（GC, 内存）也暴露出去
        DefaultExports.initialize();

        // 启动 9091 的 HTTPServer 暴露 /metrics
        new HTTPServer(9091);

        System.out.println("Prometheus metrics server started at http://localhost:9091/metrics");

        // 模拟业务循环
        while (true) {

            // 1. Gauge 用法
            activeUsers.labels("cn").set(Math.random() * 100);
            activeUsers.labels("us").inc();   // 加 1
            activeUsers.labels("eu").dec();   // 减 1

            // 2. Counter 用法（只能累加）
            requestCounter.labels("GET").inc();
            requestCounter.labels("POST").inc(2);

            // 3. Histogram 用法（测量耗时）
            Histogram.Timer timer = requestLatency.startTimer();
            try {
                // 模拟业务处理
                Thread.sleep((long) (Math.random() * 200));
            } finally {
                timer.observeDuration(); // 记录耗时
            }

            Thread.sleep(1000);
        }
    }
}

