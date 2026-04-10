package com.hzk.tool.flame;

public enum OsType {

    LINUX("linux"),
    MACOS("macos"),
    WINDOWS("windows");

    private final String displayName;

    OsType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
