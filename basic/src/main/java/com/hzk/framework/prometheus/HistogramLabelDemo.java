package com.hzk.framework.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.StringWriter;
import java.io.Writer;

public class HistogramLabelDemo {

    // 定义带 label 的 Histogram
    private static final Histogram requestLatency = Histogram.build()
            .name("demo_api_latency_seconds")
            .help("API latency in seconds")
            .labelNames("api", "method")  // label 定义
//            .buckets(0.005, 0.01, 0.05, 0.1, 0.2, 1) // 指定耗时分桶
            .register();

    public static void main(String[] args) throws Exception {
        // 启动 HTTPServer 暴露 /metrics
        new HTTPServer(9100);
        System.out.println("Prometheus metrics at http://localhost:9100/metrics");

        // 无限循环模拟业务请求
        for (int i = 0; i < 10; i++) {
            simulateRequest("user_list", "GET");
            simulateRequest("order_detail", "POST");

            Thread.sleep(500);
        }
        Writer writer = new StringWriter();
        TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
        String output = writer.toString();
        boolean flag = output.contains("demo_api_latency_seconds_bucket{");
        System.out.println(output);
    }

    // 模拟 API 请求
    private static void simulateRequest(String api, String method) {
        // 开始记录耗时（使用 label）
        Histogram.Timer timer = requestLatency.labels(api, method).startTimer();

        try {
            // 随机模拟业务处理耗时
            long costMs = (long) (Math.random() * 150);
            Thread.sleep(costMs);
        } catch (InterruptedException ignored) {
        } finally {
            // 结束计时并记录
            timer.observeDuration();
        }
    }
}

