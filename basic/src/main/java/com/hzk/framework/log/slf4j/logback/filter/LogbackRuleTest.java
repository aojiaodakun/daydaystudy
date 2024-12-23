package com.hzk.framework.log.slf4j.logback.filter;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.hzk.framework.log.slf4j.logback.LoggerNameFilter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackRuleTest {

    @Test
    public void test1() throws Exception{
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // reset
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(loggerContext);
        loggerContext.reset();
        jc.doConfigure(LogbackRuleTest.class.getResourceAsStream("/logback_dev.xml"));// StatusList会累加，可清空loggerContext.getStatusManager().clear();
        loggerContext.addTurboFilter(new RuleFilter());

        long startTimeMillis = System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger("kd.bos");
        int size = 100 * 100 * 100 * 100;// 1亿
        for (int i = 0; i < size; i++) {
            logger.info("info");
        }
        long endTimeMillis = System.currentTimeMillis();
        System.out.println(endTimeMillis - startTimeMillis);
        System.out.println("LogbackRuleTest end");
    }


    @Test
    public void test2() throws Exception{
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        // reset
        JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(loggerContext);
        loggerContext.reset();
        jc.doConfigure(LogbackRuleTest.class.getResourceAsStream("/logback_dev.xml"));// StatusList会累加，可清空loggerContext.getStatusManager().clear();
        loggerContext.addTurboFilter(new NewRuleFilter());

        long startTimeMillis = System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger("kd.bos");
        int size = 100 * 100 * 100 * 100;// 1亿
//        int size = 1;// 1亿
        for (int i = 0; i < size; i++) {
            logger.info("info");
        }
        long endTimeMillis = System.currentTimeMillis();
        System.out.println(endTimeMillis - startTimeMillis);
        System.out.println("LogbackRuleTest end");
    }


}
