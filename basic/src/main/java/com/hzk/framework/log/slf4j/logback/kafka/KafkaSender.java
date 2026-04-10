package com.hzk.framework.log.slf4j.logback.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * kafka发送类,未实现批量发送功能
 * <p>Title: KafkaSender</p>
 * <p>Description: </p>
 * <p>Company:Kingdee.com </p>
 *
 * @author rd_fuxiong_pan
 * @date 2017年4月11日 下午3:12:12
 */
public class KafkaSender<T> implements IKafkaSender<T> {
    private Properties props;
    private Producer<String, T> producer;

    public KafkaSender(Properties props) {
        this.props = props;
    }

    public Properties getProps() {
        return props;
    }

    /**
     * 发送数据到kafka
     */
    public void send(KafkaMessage<T> kafkaMsg) {
        if (producer == null) {
            producer = new KafkaProducer<>(props);
        }
        if (kafkaMsg.getTopic() == null) {
            throw new RuntimeException("Kafka producer topic can not be null");
        }
        Future<RecordMetadata> future = producer.send(new ProducerRecord<>(kafkaMsg.getTopic(), kafkaMsg.getMsg()));
        try {
            // 可能包含异常
            RecordMetadata recordMetadata = future.get();
            System.out.println(1);
        } catch (Exception e) {
            /**
             * 1、org.apache.kafka.common.errors.TimeoutException: Topic feature_dev-log not present in metadata after 10000 ms.
             * 2、
             */
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭kafka连接
     */
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

}

