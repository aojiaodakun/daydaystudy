package com.hzk.db.postgres.datasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PGUtilDemo {
    public static void main(String[] args) {
        // 1. 查询示例
        List<Map<String, Object>> users = PGSqlUtil.executeQuery(
                "SELECT * FROM users WHERE age > ?", 20);

        users.forEach(user -> {
            System.out.println(user.get("name") + ", " + user.get("email"));
        });

        // 2. 插入示例
        int rows = PGSqlUtil.executeUpdate(
                "INSERT INTO users (name, email, age) VALUES (?, ?, ?)",
                "Alice", "alice@example.com", 25);
        System.out.println("插入行数: " + rows);

        // 3. 批量插入示例
        List<Object[]> batchParams = new ArrayList<>();
        batchParams.add(new Object[]{"Bob", "bob@example.com", 30});
        batchParams.add(new Object[]{"Charlie", "charlie@example.com", 28});

        int[] batchResult = PGSqlUtil.executeBatch(
                "INSERT INTO users (name, email, age) VALUES (?, ?, ?)",
                batchParams);
        System.out.println("批量插入结果: " + Arrays.toString(batchResult));
    }
}
