package com.hzk.db.postgres.config;

import com.hzk.db.common.config.DBConfig;

public class PostgresDBConfig implements DBConfig {
    @Override
    public String driverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    public String jdbcUrl() {
        return "jdbc:postgresql://localhost:5432/test";
    }

    @Override
    public String username() {
        return "postgres";
    }

    @Override
    public String password() {
        return "postgres";
    }
}

