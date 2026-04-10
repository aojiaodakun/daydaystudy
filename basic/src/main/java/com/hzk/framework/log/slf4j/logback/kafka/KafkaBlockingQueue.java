package com.hzk.framework.log.slf4j.logback.kafka;


import java.util.concurrent.ArrayBlockingQueue;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本地缓存一个队列,后台线程进行消息发送
 * <p>Title: KafkaBlockingQueue</p>
 * <p>Description: </p>
 * <p>Company:Kingdee.com </p>
 *
 * @author rd_fuxiong_pan
 * @date 2017年4月11日 下午3:22:25
 */
public class KafkaBlockingQueue {

    private static int QUEUE_CAPACITY = 10000;
    private static ArrayBlockingQueue<KafkaMessage<?>> queue = null;
    //private static KafkaSender kafkaSender;
    private static IKafkaSender<?> kafkaSender;
    private static boolean STOPFLAG = false;

    private static final String config_prefix_key = "metric.log.kafka.prefix";

    private static String prefix = "kd.metrics.log.kafka.";

    private static final Logger logger = LoggerFactory.getLogger(KafkaBlockingQueue.class);

    public static void init(IKafkaSender<?> ikafkaSender) {
        kafkaSender = ikafkaSender;
        STOPFLAG = false;
        queue = new ArrayBlockingQueue<>(10000);
        Thread thread = new Thread(new Poll(), "log-kafka-pull");
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 添加数据队列
     *
     * @param msg
     */
    public static void offer(KafkaMessage<?> msg) {
        queue.offer(msg);
    }

    public static void stop() {
        STOPFLAG = true;
    }

    /**
     * 消息队列处理线程
     */
    static class Poll implements Runnable {

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public void run() {
            while (!STOPFLAG) {
                //by lzx, take会阻塞到有数, Thread.sleep不可取，不需要sleep，另外，201毫秒太久，瞬间可能积累太多数据
                KafkaMessage msg;
                try {
                    msg = queue.take();
//                    int i = 10/0;
                    kafkaSender.send(msg);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

                /*
                if (!queue.isEmpty()) {
                    KafkaMessage msg = queue.poll();
                    kafkaSender.send(msg);
                } else {
                    try {
                        Thread.sleep(20l);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                */
            }

        }
    }

}
