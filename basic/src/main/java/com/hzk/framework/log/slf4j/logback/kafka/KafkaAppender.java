package com.hzk.framework.log.slf4j.logback.kafka;


import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/** logback的kafaAppender类,用于格式化日志并发送消息 */
public class KafkaAppender extends KafkaLogbackConfigBase<ILoggingEvent> {
//    private static final String TRACE_NAME = "LogKafkaAppender";

    /**
     * Json的实现
     */
    protected IKafkaSender<String> kafkaSender;

    @Override
    public void start() {
        super.start();
        Properties props = getProducerProperties();
        if (syncSend) {
            kafkaSender = new KafkaSender<>(props);
        } else {
            kafkaSender = new KafkaSender<>(props);
            KafkaBlockingQueue.init(kafkaSender);
        }

    }

//    private LoggerFormat loggerFormat = new LoggerFormat();

    protected void append(ILoggingEvent event) {
//        Map<Object, Object> logObject = loggerFormat.convert(event);
        send(new KafkaMessage<>(topic, event.getFormattedMessage()));
    }

    private void send(KafkaMessage<String> msg) {
        // 如果是同步的,则直接发送
        if (syncSend) {
            kafkaSender.send(msg);
        } else {
            KafkaBlockingQueue.offer(msg);
        }
    }


    @Override
    public void stop() {
        super.stop();
        if (syncSend) {
            if (kafkaSender != null) {
                kafkaSender.close();
                kafkaSender = null;
            }
        }
    }
}

