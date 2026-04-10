package com.hzk.db.postgres.demo.query;

import com.hzk.db.postgres.datasource.PGSqlUtil;

import java.util.List;
import java.util.Map;

/**
 * 类型隐式转换异常
 * Caused by: org.postgresql.util.PSQLException: ERROR: operator does not exist: bigint = character varying
 *   建议：No operator matches the given name and argument types. You might need to add explicit type casts.
 *   位置：1098
 *     at org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2565)
 *     at org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:2297)
 *     at org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:322)
 *     at org.postgresql.jdbc.PgStatement.executeInternal(PgStatement.java:481)
 *     at org.postgresql.jdbc.PgStatement.execute(PgStatement.java:401)
 *     at org.postgresql.jdbc.PgPreparedStatement.executeWithFlags(PgPreparedStatement.java:164)
 *     at org.postgresql.jdbc.PgPreparedStatement.execute(PgPreparedStatement.java:153)
 *     at kd.bos.ksql.shell.KDPreparedStatement.execute(KDPreparedStatement.java:331)
 *     at com.zaxxer.hikari.pool.ProxyPreparedStatement.execute(ProxyPreparedStatement.java:44)
 *     at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.execute(HikariProxyPreparedStatement.java)
 *     at kd.bos.util.jdbcproxy.PreparedStatementProxy.execute(PreparedStatementProxy.java:147)
 *     at kd.bos.util.jdbcproxy.PreparedStatementProxy.execute(PreparedStatementProxy.java:147)
 *     at kd.bos.trace.instrument.jdbc.PreparedStatementAOP.execute(PreparedStatementAOP.java:37)
 *     at kd.bos.db.tx.DelegatePreparedStatement.execute(DelegatePreparedStatement.java:151)
 *     at kd.bos.db.DBImpl.query(DBImpl.java:176)
 */
public class PGTypeCastDemo {

    public static void main(String[] args) throws Exception {
        String sql = "SELECT id, name, email, age FROM users where id = ?";
        List<Map<String, Object>> maps = PGSqlUtil.executeQuery(sql, "1");// 异常
//        List<Map<String, Object>> maps = PGSqlUtil.executeQuery(sql, 1);// 正常
        System.out.println(maps);
    }

}
