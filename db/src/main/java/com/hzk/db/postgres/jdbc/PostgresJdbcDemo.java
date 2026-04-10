package com.hzk.db.postgres.jdbc;

import com.hzk.db.common.config.DBType;
import com.hzk.db.common.jdbc.AbstractJdbcDemo;

public class PostgresJdbcDemo extends AbstractJdbcDemo {

    @Override
    public String dbType() {
        return DBType.postgresql.getId();
    }

    @Override
    protected String createTableSql() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "age INTEGER)";
        return createTableSQL;
    }

    public static void main(String[] args) {
        PostgresJdbcDemo jdbcDemo = new PostgresJdbcDemo();
        jdbcDemo.crud();
    }

}
