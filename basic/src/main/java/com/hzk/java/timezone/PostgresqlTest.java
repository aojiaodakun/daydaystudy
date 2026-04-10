package com.hzk.java.timezone;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;

public class PostgresqlTest {

    static Connection connection;

    @Before
    public void before() throws Exception{
        Class.forName("org.postgresql.Driver");
        String ip = "172.20.158.202";
        int port = 5432;
        String dbInstance = "t_zonetest";
        String user = "postgres";
        String passwd = "postgres";

        String url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbInstance + "?user=" + user + "&password=" + passwd;
        String params = "&useCursorFetch=true&defaultRowFetchSize=5000";
        String finalUrl = url + params;
        connection = DriverManager.getConnection(finalUrl);
    }

    /**
     * -Duser.timezone=Asia/Shanghai
     * -Duser.timezone=Europe/Berlin
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
        System.out.println("jvm时区：" + System.getProperty("user.timezone"));
        // 当前会话时区
        String serverTimeZoneSql = "show timezone;";
        PreparedStatement preparedStatement = connection.prepareStatement(serverTimeZoneSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String timezone = resultSet.getString("timezone");
            System.out.println("当前会话时区：" + timezone);
        }

        // 数值查询
        String sql = "select id,number,fcreatetime from t_zonetest where number = 'hzk3';";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {

            int id = resultSet.getInt("id");
            System.out.println("id:" + id);
            Date createtime = resultSet.getDate("fcreatetime");
            System.out.println("resultSet.getDate:" + createtime);
            System.out.println("resultSet.getDate.getTime():" + createtime.getTime());

            Time fcreatetime = resultSet.getTime("fcreatetime");
            System.out.println("resultSet.getTime():" + fcreatetime);
            System.out.println("resultSet.getTime().getTime():" + fcreatetime.getTime());
            System.out.println("resultSet.getTimestamp.getTime():" + resultSet.getTimestamp("fcreatetime").getTime());

            System.out.println("----------------------------");
        }
        resultSet.close();
        preparedStatement.close();
        connection.close();


    }

    /**
     * -Duser.timezone=Asia/Shanghai
     * -Duser.timezone=Europe/Berlin
     * @throws Exception
     */
    @Test
    public void test2() throws Exception{
        System.out.println("jvm时区：" + System.getProperty("user.timezone"));
        // 当前会话时区
        String insertSql = "insert into t_zonetest(id,number,fcreatetime) values(?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
        preparedStatement.setInt(1, 3);
        preparedStatement.setString(2, "hzk3");
        // 1657698245589
        // shanghai:1657698245589
        // Berlin:1657719845589

        // 1657698359286
        // shanghai:1657676759286
        // Berlin:1657698359286
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("currentTimeMillis:" + currentTimeMillis);
        preparedStatement.setTimestamp(3, new Timestamp(currentTimeMillis));

        int size = preparedStatement.executeUpdate();
        System.out.println(size);

        preparedStatement.close();
        connection.close();

    }


}
