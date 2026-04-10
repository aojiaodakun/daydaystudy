package com.hzk.tool.flame;

public enum CpuArch {
    X64("x64"),
    X86("x86"),
    ARM64("arm64"),
    ARM32("arm32"),
    PPC64LE("ppc64le"),
    RISCV64("riscv64"),
    LOONGARCH64("loongarch64");

    private final String canonicalName;

    CpuArch(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getCanonicalName() {
        return canonicalName;
    }
}

