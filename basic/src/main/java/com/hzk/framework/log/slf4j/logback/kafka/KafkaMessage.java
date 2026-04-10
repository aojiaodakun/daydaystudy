package com.hzk.framework.log.slf4j.logback.kafka;

public class KafkaMessage<T> {
    private String topic;
    private T msg;

    public KafkaMessage(String topic, T msg) {
        this.topic = topic;
        this.msg = msg;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}

