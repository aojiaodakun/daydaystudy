package com.hzk.framework.log.slf4j.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.util.StatusPrinter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LogConfigTest {

    @Test
    public void logconfigTest() throws Exception {
        // logback.xml
        Logger log1 = LoggerFactory.getLogger("stdot-only1");
        log1.error("aaa");

//        boolean flag = true;
//        while (flag) {
//            log1.error("bbb");
//            TimeUnit.SECONDS.sleep(1);
//        }

        // logback_bak.xml
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.addTurboFilter(new LoggerNameFilter());// 自定义过滤器
        Appender<ILoggingEvent> consoleAppender1 = null;
        List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
        for(ch.qos.logback.classic.Logger tempLogger : loggerList) {
            Appender<ILoggingEvent> tempStdotAppender = tempLogger.getAppender("stdot");
            if (tempStdotAppender != null) {
                consoleAppender1 = tempStdotAppender;
                break;
            }
        }
        if (consoleAppender1 != null) {
            // 1、动态注入stdot的Logger对象，用户可直接引用
            Logger stdotLog = LoggerFactory.getLogger("stdot");// parent=ROOT
            ch.qos.logback.classic.Logger stdotLogLogbackLogger = (ch.qos.logback.classic.Logger)stdotLog;
            stdotLogLogbackLogger.addAppender(consoleAppender1);
            stdotLogLogbackLogger.setAdditive(true);// 可叠加

            // 2、动态注入stdot-only的Logger对象，用户可直接引用
            Logger stdotOnlyLog = LoggerFactory.getLogger("stdot-only");// parent=ROOT
            ch.qos.logback.classic.Logger stdotOnlyLogbackLogger = (ch.qos.logback.classic.Logger)stdotOnlyLog;
            stdotOnlyLogbackLogger.addAppender(consoleAppender1);
            stdotOnlyLogbackLogger.setAdditive(false);// 可叠加
        }
        log1.info("info log");

//        String config = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><configuration><appender name=\"stdot\" class=\"ch.qos.logback.core.ConsoleAppender\"><layout class=\"ch.qos.logback.classic.PatternLayout\"><pattern>%d{HH:mm:ss} [%thread] %level %logger.%M(%L):%m%n</pattern></layout></appender><root level=\"INFO\"><appender-ref ref=\"stdot\" /></root></configuration>";
        String config = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><configuration><appender name=\"stdot\" class=\"ch.qos.logback.core.ConsoleAppender\"><layout class=\"ch.qos.logback.classic.PatternLayout\"><pattern>%d{HH:mm:ss} [%thread] %level %logger.%M\\(%L\\):%m%n</pattern></layout></appender><root level=\"INFO\"><appender-ref ref=\"stdot\" /></root></configuration>";

        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(loggerContext);
        loggerContext.reset();
        try {
            jc.doConfigure(new ByteArrayInputStream(config.getBytes("utf-8")));
//            jc.doConfigure(new FileInputStream("D:\\project\\log-test\\logback_demo\\src\\main\\resources\\logback_dev.xml"));
            System.out.println("--------------new------------");
            Appender<ILoggingEvent> consoleAppender2 = null;
            List<ch.qos.logback.classic.Logger> loggerList2 = loggerContext.getLoggerList();
            for(ch.qos.logback.classic.Logger tempLogger : loggerList2) {
                Appender<ILoggingEvent> tempStdotAppender = tempLogger.getAppender("stdot");
                if (tempStdotAppender != null) {
                    consoleAppender2 = tempStdotAppender;
                    break;
                }
            }

            StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }



        log1.info("info log");
        log1.info("info log");
        log1.info("info log");
        System.in.read();


    }

}
