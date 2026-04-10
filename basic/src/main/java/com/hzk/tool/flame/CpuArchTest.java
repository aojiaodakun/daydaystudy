package com.hzk.tool.flame;

import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * async-profiler 官方支持的 CPU 架构测试
 * https://github.com/async-profiler/async-profiler
 */
public class CpuArchTest {

    public static void main(String[] args) throws Exception {
        String cpuArchConfig = "x64=x86_64,amd64&x86=x86,i386,i486,i586,i686&arm64=aarch64,arm64";
        cpuArchConfig = "x64=x86_64,amd64\n" +
                "x86=x86,i386,i486,i586,i686\n" +
                "arm64=aarch64,arm64";
        Properties properties = new Properties();
        properties.load(new StringReader(cpuArchConfig));
        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
        for(Map.Entry<Object, Object> entry : entrySet) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "=" + value);
        }
        String osArch = "amd64";

        if (!AsyncProfilerPlatformDetector.isAsyncProfilerAvailable()) {
            throw new UnsupportedOperationException(
                    "Async-profiler is not available on current platform: " +
                            AsyncProfilerPlatformDetector.platformSummary()
            );
        }
    }

}
