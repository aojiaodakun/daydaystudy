package com.hzk.tool.flame;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class AsyncProfilerPlatformDetector {

    /* ---------------- supported matrix ---------------- */

    // async-profiler 官方支持的 CPU 架构
    private static final EnumSet<CpuArch> SUPPORTED_ARCHS = EnumSet.of(
            CpuArch.X64,
            CpuArch.X86,
            CpuArch.ARM64,
            CpuArch.ARM32,
            CpuArch.PPC64LE,
            CpuArch.RISCV64,
            CpuArch.LOONGARCH64
    );

    // async-profiler 支持的操作系统
    private static final EnumSet<OsType> SUPPORTED_OS = EnumSet.of(
            OsType.LINUX,
            OsType.MACOS
            // Windows 官方不支持（需要 WSL）
    );

    /* ---------------- arch mapping ---------------- */

    private static final Map<String, CpuArch> ARCH_MAPPING = new HashMap<>();

    // x64=x86_64,amd64&x86=x86,i386,i486,i586,i686&arm64=aarch64,arm64
    static {
        // x64
        map(CpuArch.X64, "x86_64", "amd64");

        // x86
        map(CpuArch.X86, "x86", "i386", "i486", "i586", "i686");

        // arm64
        map(CpuArch.ARM64, "aarch64", "arm64");

        // arm32
        map(CpuArch.ARM32, "arm", "armv6", "armv7", "armv7l");

        // others
        map(CpuArch.PPC64LE, "ppc64le", "powerpc64le");
        map(CpuArch.RISCV64, "riscv64");
        map(CpuArch.LOONGARCH64, "loongarch64");
    }

    private static void map(CpuArch arch, String... aliases) {
        for (String alias : aliases) {
            ARCH_MAPPING.put(alias, arch);
        }
    }

    private AsyncProfilerPlatformDetector() {
    }

    /* ---------------- public API ---------------- */

    /** async-profiler 是否整体可用 */
    public static boolean isAsyncProfilerAvailable() {
        return isSupportedArch() && isSupportedOs();
    }

    /** 当前 CPU 架构是否被 async-profiler 支持 */
    public static boolean isSupportedArch() {
        return detectCpuArch()
                .map(SUPPORTED_ARCHS::contains)
                .orElse(false);
    }

    /** 当前操作系统是否被 async-profiler 支持 */
    public static boolean isSupportedOs() {
        return detectOsType()
                .map(SUPPORTED_OS::contains)
                .orElse(false);
    }

    /** 检测当前 CPU 架构（归一化后） */
    public static Optional<CpuArch> detectCpuArch() {
        String osArch = System.getProperty("os.arch");
        return Optional.ofNullable(
                ARCH_MAPPING.get(osArch.toLowerCase(Locale.ROOT))
        );
    }

    /** 返回当前平台的 canonical 描述（用于日志/异常） */
    public static String platformSummary() {
        String arch = detectCpuArch()
                .map(CpuArch::getCanonicalName)
                .orElse("unknown");

        String os = detectOsType()
                .map(OsType::getDisplayName)
                .orElse("unknown");

        return os + "/" + arch +
                " (os.name=" + System.getProperty("os.name") +
                ", os.arch=" + System.getProperty("os.arch") + ")";
    }

    /* ---------------- OS detection ---------------- */

    private static Optional<OsType> detectOsType() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            return Optional.empty();
        }
        String name = osName.toLowerCase(Locale.ROOT);
        if (name.contains("linux")) {
            return Optional.of(OsType.LINUX);
        }
        if (name.contains("mac") || name.contains("darwin")) {
            return Optional.of(OsType.MACOS);
        }
        if (name.contains("windows")) {
            return Optional.of(OsType.WINDOWS);
        }
        return Optional.empty();
    }
}

