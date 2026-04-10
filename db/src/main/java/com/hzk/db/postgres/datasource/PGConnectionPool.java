package com.hzk.db.postgres.datasource;

import com.hzk.db.common.config.DBConfig;
import com.hzk.db.common.config.DBConfigFactory;
import com.hzk.db.common.config.DBType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class PGConnectionPool {
    private static HikariDataSource dataSource;

    // 初始化连接池
    static {
        HikariConfig config = new HikariConfig();
        DBConfig dbConfig = DBConfigFactory.getDBConfig(DBType.postgresql.getId());
        config.setDriverClassName(dbConfig.driverClass());
        config.setJdbcUrl(dbConfig.jdbcUrl());
        config.setUsername(dbConfig.username());
        config.setPassword(dbConfig.password());

        // 连接池配置
        String datasourceConfig = "datasource.type=hikari\n" +
                "minimumIdle=4\n" +
                "maximumPoolSize=60\n" +
                "idleTimeout=600000\n" +
                "validationTimeout=10000\n" +
                "maxLifetime=1800000\n" +
                "connectionTimeout=120000";
        Properties prop = new Properties();
        StringReader reader = new StringReader(datasourceConfig);
        try {
            prop.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("maximumPoolSize")));// 最大连接数
        config.setMinimumIdle(Integer.parseInt(prop.getProperty("minimumIdle")));// 最小空闲连接
        config.setIdleTimeout(Long.parseLong(prop.getProperty("idleTimeout")));// 空闲连接超时时间(ms)
        config.setConnectionTimeout(Long.parseLong(prop.getProperty("connectionTimeout")));// 连接超时时间(ms)
        config.setMaxLifetime(Long.parseLong(prop.getProperty("maxLifetime")));// 连接最大存活时间(ms)
        config.setValidationTimeout(Long.parseLong(prop.getProperty("validationTimeout")));// 连接验证操作（检查连接是否仍然有效）的最大等待时间（ms）
        config.setPoolName("PG-HikariPool");// 连接池名称

        // 可选: PostgreSQL 特定配置
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    // 获取连接
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 关闭连接池
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
