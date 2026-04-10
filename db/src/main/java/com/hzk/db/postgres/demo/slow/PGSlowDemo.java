package com.hzk.db.postgres.demo.slow;

import com.hzk.db.common.config.DBConfig;
import com.hzk.db.common.config.DBConfigFactory;
import com.hzk.db.common.config.DBType;
import com.hzk.db.common.jdbc.AbstractJdbcDemo;
import com.hzk.db.postgres.datasource.PGConnectionPool;
import com.hzk.db.postgres.datasource.PGSqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * pg库慢sql案例
 * select pid,datname,usename,state,wait_event_type,
 * wait_event, query_start,
 * now()-query_start as sql_time,query
 * from pg_stat_activity
 * where state <> 'idle'
 * order by 8 desc;
 */
public class PGSlowDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(PGSlowDemo.class.getName());

    private static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    private static Random random = new Random();

    public static void main(String[] args) throws Exception {
//        slow1();
        slow2();
    }

    /**
     * 单个连接sleep 10秒
     */
    private static void slow1() throws Exception {
        String sql = "SELECT pg_sleep(10);";
        List<Map<String, Object>> maps = PGSqlUtil.executeQuery(sql);
        System.out.println(maps);
        LOGGER.info("slow1 end");
    }


    /**
     * 10个连接sleep n秒
     */
    private static void slow2() throws Exception {
        int size = 10;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            THREAD_POOL.execute(() ->{
                int randomInt = random.nextInt(20);
                int sleepSeconds = Math.max(10, randomInt);
                LOGGER.info(Thread.currentThread().getName() + " start,sleepSeconds=" + sleepSeconds);
                String sql = "SELECT pg_sleep(" + sleepSeconds + ");";
                PGSqlUtil.executeQuery(sql);
                try {
                    countDownLatch.countDown();
                } catch (Exception e) {}
            });
        }
        countDownLatch.await();
        LOGGER.info("slow2 end");
    }
}
