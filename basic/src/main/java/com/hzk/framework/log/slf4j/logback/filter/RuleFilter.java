package com.hzk.framework.log.slf4j.logback.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 有性能问题
 */
public class RuleFilter extends TurboFilter {

    private static final AtomicReference<List<Rule>> allRule = new AtomicReference<>(Collections.emptyList());

    static {
        /**
         * 10条规则
         * 过期：5
         * 未到启用时间：2
         * 有效期，有上下文className:2
         * 有效期，无className:1
         */
        /**
         * 过期：5
         */
        List<Rule> tempList = new ArrayList<>();
        Date currDate = new Date();
        long currentTimeMillis = System.currentTimeMillis();
        Rule rule1 = new Rule();
        rule1.setEndTime(currentTimeMillis);
        tempList.add(rule1);
        Rule rule2 = new Rule();
        rule2.setEndTime(currentTimeMillis);
        tempList.add(rule2);
        Rule rule3 = new Rule();
        rule3.setEndTime(currentTimeMillis);
        tempList.add(rule3);
        Rule rule4 = new Rule();
        rule4.setEndTime(currentTimeMillis);
        tempList.add(rule4);
        Rule rule5 = new Rule();
        rule5.setEndTime(currentTimeMillis);
        tempList.add(rule5);
        /**
         * 未到启用时间：2
         */
        Date oneHourAfterCurrDate = DateUtils.addHours(currDate, 1);
        Rule rule6 = new Rule();
        rule6.setStartTime(oneHourAfterCurrDate.getTime());
        tempList.add(rule6);
        Rule rule7 = new Rule();
        rule7.setStartTime(oneHourAfterCurrDate.getTime());
        tempList.add(rule7);
        /**
         * 有效期，有上下文className:2
         */
        Rule rule8 = new Rule();
        rule8.setStartTime(currentTimeMillis);
        rule8.setEndTime(oneHourAfterCurrDate.getTime());
        rule8.setClassName("kd");
        tempList.add(rule8);
        Rule rule9 = new Rule();
        rule9.setStartTime(currentTimeMillis);
        rule9.setEndTime(oneHourAfterCurrDate.getTime());
        rule9.setClassName("kd.bos");
        tempList.add(rule9);
        /**
         * 有效期，无className:1
         */
        Rule rule10 = new Rule();
        rule10.setStartTime(currentTimeMillis);
        rule10.setEndTime(oneHourAfterCurrDate.getTime());
        tempList.add(rule10);

        allRule.set(tempList);
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        return decide(logger, level);
    }

    public static FilterReply decide(Logger logger, Level level) {
        // 获取匹配的规则，按className前缀过滤
        List<Rule> ruleList = getRules(logger);
        if (ruleList.isEmpty()) {
            return FilterReply.NEUTRAL;
        }
        String appId = null;
        String appName = null;
        String accountId = null;
        String tenantId = null;
        String formId = null;
        String userId = null;
        String userName = null;
//        OperationContext oc = OperationContext.get();
//        if (oc != null) {
//            appId = oc.getAppId();
//            tenantId = oc.getTenantId();
//            formId = oc.getFormId();
//        }
//        //noinspection unchecked
//        Map<String, String> truck = (Map<String, String>) ThreadTruck.get(CoreConstants.KEY_REQUESTCONTEXT_INFO);
//        if (truck != null) {
//            accountId = truck.get("accountId");
//            userId = truck.get("userId");
//            userName = truck.get("userName");
//        }
        Rule matchRule = null;
        for (Rule rule : ruleList) {
            if (matchRule != null) {
                if (matchRule.getLevel() <= rule.getLevel()) {
                    continue;
                }
                if (matchRule.getSort() != rule.getSort()) {
                    break;
                }
            }
            if (match(rule, appId, appName, accountId, tenantId, formId, userId, userName)) {
                matchRule = rule;
            }
        }
        if (matchRule != null) {
            return getFilterReply(matchRule.getLevel(), level);
        }
        return FilterReply.NEUTRAL;
    }

    // 获取匹配的rule，按className
    private static List<Rule> getRules(Logger logger) {
        List<Rule> ruleList = new ArrayList<>();
        if (logger == null) {
            return ruleList;
        }
        long currentTimeMillis = System.currentTimeMillis();
        String loggerName = logger.getName();
        for (Rule rule : allRule.get()) {
            if (rule.getStartTime() > 0 && rule.getStartTime() > currentTimeMillis) {
                // 未到启动时间
                continue;
            }
            if (rule.getEndTime() > 0 && rule.getEndTime() < currentTimeMillis) {
                // 已过期
                continue;
            }
            if (rule.getClassName() == null) {
                ruleList.add(rule);
            } else if (loggerName.startsWith(rule.getClassName())) {// 目前主要耗时点，但规则应该不多，目前耗时可以接受
                ruleList.add(rule);
            }
        }
        return ruleList;
    }

    private static boolean match(Rule rule, String appId, String appName, String accountId, String tenantId,
                                 String formId, String userId, String userName) {
        return equals(rule.getAppId(), appId)
                && equals(rule.getAppName(), appName)
                && equals(rule.getAccountId(), accountId)
                && equals(rule.getTenantId(), tenantId)
                && equals(rule.getFormId(), formId)
                && equals(rule.getUserId(), userId)
                && equals(rule.getUserName(), userName);
    }

    private static FilterReply getFilterReply(int expectLevel, Level level) {
        if (expectLevel <= level.levelInt) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }

    private static boolean equals(String expect, String actual) {
        if (expect == null) {// 为null，则是没配置，返回true
            return true;
        } else {
            return Objects.equals(expect, actual);
        }
    }

}
