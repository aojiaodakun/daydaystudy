package com.hzk.db.common.config;

import com.hzk.db.mysql.config.MySqlDBConfig;
import com.hzk.db.postgres.config.PostgresDBConfig;

public class DBConfigFactory {

    public static DBConfig getDBConfig(String dbType) {
        switch (dbType) {
            case "mysql" :
                return new MySqlDBConfig();
            case "postgresql" :
                return new PostgresDBConfig();
        }
        throw new IllegalArgumentException("dbType is illegal,dbType=" + dbType);
    }

}
