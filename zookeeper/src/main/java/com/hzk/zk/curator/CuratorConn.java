package com.hzk.zk.curator;

import com.hzk.zk.constants.BasicConstants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;

public class CuratorConn {

    public static void main(String[] args) {
        // 3秒后重试一次，只连1次
        RetryPolicy retryPolicy = new RetryOneTime(1000 * 3);

        // 每3秒重连一次，重试3次
        RetryPolicy retryPolicy1 = new RetryNTimes(3,1000 * 3);

        // 每3秒重连一次，总等待时间超过10秒后停止重连
        RetryPolicy retryPolicy2 = new RetryUntilElapsed(1000 * 10,1000 * 3);

        // 创建连接对象
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(BasicConstants.IP_CLUSTER)
                .sessionTimeoutMs(1000 * 10)
                .retryPolicy(new RetryOneTime(1000 *3))
                .namespace("create")
                .build();
        client.start();
        System.out.println(client.isStarted());
        client.close();
    }

}
