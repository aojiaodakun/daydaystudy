package com.hzk.framework.log.slf4j.logback;

import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class KafkaAppender<E> extends UnsynchronizedAppenderBase<E> {
    @Override
    protected void append(E eventObject) {
//        System.out.println("kafka:" + eventObject);
    }

}
