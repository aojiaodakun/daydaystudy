package com.hzk.db.common.config;

public enum DBType {

    postgresql("postgresql"),

    mysql("mysql"),

    ;

    private String id;

    private DBType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
