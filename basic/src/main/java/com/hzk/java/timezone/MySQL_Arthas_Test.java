package com.hzk.java.timezone;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MySQL_Arthas_Test {

    public static void main(String[] args) throws Exception{
        String serverTimezone = "Asia/Shanghai";
        final String common = "serverTimezone=" + serverTimezone
                + "&zeroDateTimeBehavior=round&useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true";
        final String fetch = "&allowMultiQueries=true&defaultFetchSize=1000&useCursorFetch=true&useServerPrepStmts=true&cachePrepStmts=false&prepStmtCacheSize=5&prepStmtCacheSqlLimit=4096";
        // 优化批量插入
        String allowLoadLocalInfileUrl = "&allowLoadLocalInfile=false";
        String connUrl = "jdbc:mysql://127.0.0.1:3306/db2019?" + common + allowLoadLocalInfileUrl + fetch;

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(connUrl);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        Connection connection = dataSource.getConnection();

        String sql = "select id,serial from payment where id = 31;";

        while (true) {
            Thread.currentThread().sleep(1000 * 1);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        }


//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            int id = resultSet.getInt("id");
//            System.out.println(id);
//        }
//        resultSet.close();
//        preparedStatement.close();
//        connection.close();


    }

}
