package com.hzk.db.common.jdbc;

import com.hzk.db.common.config.DBConfig;
import com.hzk.db.common.config.DBConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractJdbcDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJdbcDemo.class.getName());

    // PostgreSQL 连接信息
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    protected abstract String dbType();

    protected void crud(){
        Connection connection = null;
        try {
            DBConfig dbConfig = DBConfigFactory.getDBConfig(dbType());
            // 1. 加载并注册JDBC驱动
            Class.forName(dbConfig.driverClass());

            // 2. 建立数据库连接
            connection = DriverManager.getConnection(dbConfig.jdbcUrl(), dbConfig.username(), dbConfig.password());
            LOGGER.info("成功连接到" + dbType() + "数据库!");

            // 3. 创建表
            createTable(connection);

            // 4. 插入数据
            insertData(connection, "张三", "zhangsan@example.com", 25);
            insertData(connection, "李四", "lisi@example.com", 30);
            insertData(connection, "王五", "wangwu@example.com", 28);

            // 5. 查询数据
            LOGGER.info("插入数据后查询结果:");
            queryData(connection);

            // 6. 更新数据
            updateData(connection, 1, "张三 Updated", "zhangsan.updated@example.com", 26);
            LOGGER.info("\n更新数据后查询结果:");
            queryData(connection);

            // 7. 删除数据
            deleteData(connection, 3);
            LOGGER.info("\n删除数据后查询结果:");
            queryData(connection);

        } catch (ClassNotFoundException e) {
            System.err.println("找不到" + dbType() + "的JDBC驱动!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("数据库连接或操作错误!");
            e.printStackTrace();
        } finally {
            // 8. 关闭连接
            if (connection != null) {
                try {
                    connection.close();
                    LOGGER.info("\n数据库连接已关闭!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 创建表
    protected void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql());
            LOGGER.info("表创建成功或已存在!");
        }
    }

    protected abstract String createTableSql();

    // 插入数据
    private void insertData(Connection connection, String name,
                                   String email, int age) throws SQLException {
        String insertSQL = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, age);

            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("插入了 " + rowsAffected + " 行数据: " + name);
        }
    }

    // 查询数据
    private void queryData(Connection connection) throws SQLException {
        String querySQL = "SELECT id, name, email, age FROM users";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(querySQL)) {

            LOGGER.info("ID\tName\t\tEmail\t\t\tAge");
            LOGGER.info("---------------------------------------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int age = resultSet.getInt("age");

                System.out.printf("%d\t%-10s\t%-20s\t%d%n", id, name, email, age);
            }
        }
    }

    // 更新数据
    private void updateData(Connection connection, int id, String name,
                                   String email, int age) throws SQLException {
        String updateSQL = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, age);
            preparedStatement.setInt(4, id);

            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("更新了 " + rowsAffected + " 行数据: ID=" + id);
        }
    }

    // 删除数据
    private void deleteData(Connection connection, int id) throws SQLException {
        String deleteSQL = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            LOGGER.info("删除了 " + rowsAffected + " 行数据: ID=" + id);
        }
    }

}
