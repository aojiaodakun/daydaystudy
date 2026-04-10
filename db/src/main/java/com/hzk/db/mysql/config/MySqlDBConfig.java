package com.hzk.db.mysql.config;

import com.hzk.db.common.config.DBConfig;

public class MySqlDBConfig implements DBConfig {
    @Override
    public String driverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String jdbcUrl() {
        return "jdbc:mysql://localhost:3306/test";
    }

    @Override
    public String username() {
        return "root";
    }

    @Override
    public String password() {
        return "root";
    }
}
