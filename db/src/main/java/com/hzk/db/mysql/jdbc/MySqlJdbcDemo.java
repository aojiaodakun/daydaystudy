package com.hzk.db.mysql.jdbc;

import com.hzk.db.common.config.DBType;
import com.hzk.db.common.jdbc.AbstractJdbcDemo;

public class MySqlJdbcDemo extends AbstractJdbcDemo {

    public static void main(String[] args) {
        MySqlJdbcDemo jdbcDemo = new MySqlJdbcDemo();
        jdbcDemo.crud();
    }

    @Override
    public String dbType() {
        return DBType.mysql.getId();
    }

    @Override
    protected String createTableSql() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "age INTEGER)";
        return createTableSQL;
    }

}
