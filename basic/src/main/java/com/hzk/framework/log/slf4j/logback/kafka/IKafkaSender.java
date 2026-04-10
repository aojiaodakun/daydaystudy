package com.hzk.framework.log.slf4j.logback.kafka;

public interface IKafkaSender<T> {

    public void send(KafkaMessage<T> kafkaMsg);

    public void close() ;
}
