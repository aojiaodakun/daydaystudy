package com.hzk.framework.log.slf4j.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {

    public static void main(String[] args) throws Exception {
        // logback.xml
        Logger logger = LoggerFactory.getLogger(LogbackTest.class.getName());
        logger.info("logback info");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getStatusManager().getCopyOfStatusList().forEach(e-> System.out.println(e));
        System.out.println("----------------------------------------------");

        // reset,logback_dev.xml
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(loggerContext);
        loggerContext.reset();
        jc.doConfigure(LogbackTest.class.getResourceAsStream("/logback_dev.xml"));// StatusList会累加，可清空loggerContext.getStatusManager().clear();
        loggerContext.addTurboFilter(new LoggerNameFilter());
        loggerContext.getStatusManager().getCopyOfStatusList().forEach(e-> System.out.println(e));
        logger.info("logback info2");// 多个kafkaAppender

        Logger logger2 = LoggerFactory.getLogger("org.apache.log2");
        logger2.info("apache info");// LoggerNameFilter返回DENY，被过滤



    }

}
