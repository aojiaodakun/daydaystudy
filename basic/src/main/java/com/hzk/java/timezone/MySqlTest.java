package com.hzk.java.timezone;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class MySqlTest {


    @Before
    public void before() throws Exception{

    }

    /**
     * -Duser.timezone=Asia/Shanghai
     * -Duser.timezone=Europe/Berlin
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
        String serverTimezone = "Asia/Shanghai";
//        String serverTimezone = "Europe/Berlin";
        // rewriteBatchedStatements必须设置为false，否则在批量执行时BigDecimal类型会有精度丢失问题
        final String common = "serverTimezone=" + serverTimezone
                + "&zeroDateTimeBehavior=round&useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true";
        final String fetch = "&allowMultiQueries=true&defaultFetchSize=1000&useCursorFetch=true&useServerPrepStmts=true&cachePrepStmts=false&prepStmtCacheSize=5&prepStmtCacheSqlLimit=4096";
        // 优化批量插入
        String allowLoadLocalInfileUrl = "&allowLoadLocalInfile=false";
        String connUrl = "jdbc:mysql://172.20.158.202:3306/zonetestdb?" + common + allowLoadLocalInfileUrl + fetch;

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(connUrl);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        Connection connection = dataSource.getConnection();

        String sql = "select id,number,fcreatetime from t_zonetest where number = 'hzk3';";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);


        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            Date createtime = resultSet.getDate("fcreatetime");
            System.out.println("resultSet.getDate:" + createtime);
            System.out.println("resultSet.getDate.getTime():" + createtime.getTime());

            Time fcreatetime = resultSet.getTime("fcreatetime");
            System.out.println("resultSet.getTime():" + fcreatetime);
            System.out.println("resultSet.getTime().getTime():" + fcreatetime.getTime());

            System.out.println("resultSet.getTimestamp.getTime():" + resultSet.getTimestamp("fcreatetime").getTime());
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }


    @Test
    public void test2() throws Exception{
        String dateString = "2022-07-06 19:37:12";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = df.parse(dateString);
        System.out.println(date.getTime());

//        Calendar calendar = Calendar.getInstance();
//        java.util.Date time = calendar.getTime();
//        System.out.println(time.getTime());
        long dbTime1 = date.getTime();
        long berlinTime2 = 1657129032000L;
        long betweenTime3 = dbTime1 - berlinTime2;
        System.out.println(betweenTime3);
        long hour = Math.abs(betweenTime3)/60/60/1000;
        System.out.println("小时数:" + hour);
    }

    @Test
    public void test3() throws Exception{
        TimeZone timeZone8 = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone timeZone2 = TimeZone.getTimeZone("Europe/Berlin");

        java.util.Date date = new java.util.Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        df.setTimeZone(timeZone8);
        String dateString8 = df.format(date);
        System.out.println("time8:" + dateString8);

        df.setTimeZone(timeZone2);
        String dateString2 = df.format(date);
        System.out.println("time2:" + dateString2);

    }


    @Test
    public void testInsertInto() throws Exception{
//        String serverTimezone = "Asia/Shanghai";
        String serverTimezone = "Europe/Berlin";
        // rewriteBatchedStatements必须设置为false，否则在批量执行时BigDecimal类型会有精度丢失问题
        final String common = "serverTimezone=" + serverTimezone
                + "&zeroDateTimeBehavior=round&useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true";
        final String fetch = "&allowMultiQueries=true&defaultFetchSize=1000&useCursorFetch=true&useServerPrepStmts=true&cachePrepStmts=false&prepStmtCacheSize=5&prepStmtCacheSqlLimit=4096";
        // 优化批量插入
        String allowLoadLocalInfileUrl = "&allowLoadLocalInfile=false";
        String connUrl = "jdbc:mysql://172.20.158.202:3306/zonetestdb?" + common + allowLoadLocalInfileUrl + fetch;
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(connUrl);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        Connection connection = dataSource.getConnection();

        // 当前会话时区
        String insertSql = "insert into t_zonetest(id,number,fcreatetime) values(?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, 4);
        preparedStatement.setString(2, "hzk4");
        /**
         * 1657848203540
         * shanghai:1657848204000
         * Berlin:1657869804000
         */

        /**
         * 1657848663579
         * shanghai:1657827064000
         * Berlin:1657848664000
         */

        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("currentTimeMillis:" + currentTimeMillis);
        preparedStatement.setTimestamp(3, new Timestamp(currentTimeMillis));
        int size = preparedStatement.executeUpdate();
        System.out.println(size);

        preparedStatement.close();
        connection.close();


    }


}
