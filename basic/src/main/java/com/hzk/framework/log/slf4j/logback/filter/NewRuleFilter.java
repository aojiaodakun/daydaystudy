package com.hzk.framework.log.slf4j.logback.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Map<className, RuleNode链>优化
 */
public class NewRuleFilter extends TurboFilter {

    private static final AtomicReference<Map<String, RuleNode>> className_ruleNode_map = new AtomicReference<>(Collections.emptyMap());
    private static final AtomicReference<Map<String, List<Rule>>> className_availableRuleList_map = new AtomicReference<>(Collections.emptyMap());

    private static final String ROOT = "ROOT";

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
        rule1.setClassName("kd.bos.mq.pigeon");
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

        Rule rule10 = new Rule();
        rule10.setStartTime(currentTimeMillis);
        rule10.setEndTime(oneHourAfterCurrDate.getTime());
        rule10.setClassName("com.hzk");
        tempList.add(rule10);
        /**
         * 有效期，无className:1
         */
        Rule rule11 = new Rule();
        rule11.setStartTime(currentTimeMillis);
        rule11.setEndTime(oneHourAfterCurrDate.getTime());
        tempList.add(rule11);


        Map<String, RuleNode> new_className_ruleNode_map = new HashMap<>();
        RuleNode rootRuleNode = new RuleNode();
        new_className_ruleNode_map.put(ROOT, rootRuleNode);// 顶级ROOT
        List<String> allClassNameList = new ArrayList<>();
        for (Rule tempRule : tempList) {
            // 排除appName不一致Rule,TODO

            String tempClassName = tempRule.getClassName();
            if (StringUtils.isEmpty(tempClassName)) {
                tempClassName = ROOT;
            } else {
                if (!allClassNameList.contains(tempClassName)) {
                    allClassNameList.add(tempClassName);
                }
            }
            RuleNode tempRuleNode = new_className_ruleNode_map.computeIfAbsent(tempClassName, key -> new RuleNode());
            tempRuleNode.setClassName(tempClassName);
            if (tempRule.getStartTime() > 0 && tempRule.getStartTime() > currentTimeMillis) {
                // 未到启动时间
                tempRuleNode.getIgnoreRuleList().add(tempRule);
                continue;
            }
            if (tempRule.getEndTime() > 0 && tempRule.getEndTime() < currentTimeMillis) {
                // 已过期
                tempRuleNode.getIgnoreRuleList().add(tempRule);
                continue;
            }
            tempRuleNode.getAvaiableRuleList().add(tempRule);
        }
        allClassNameList.sort((o1, o2) -> o2.split("\\.").length - o1.split("\\.").length);

        List<String> copyAllClassNameList = new ArrayList<>(allClassNameList.size());
        copyAllClassNameList.addAll(allClassNameList);
        // 构建树结构
        Iterator<String> classNameIt = allClassNameList.iterator();
        while (classNameIt.hasNext()) {
            String tempClassName = classNameIt.next();
            RuleNode tempRuleNode = new_className_ruleNode_map.get(tempClassName);
            if (!tempClassName.contains(".")) {// 单级包绑定ROOT
                tempRuleNode.setParentRuleNode(rootRuleNode);
                classNameIt.remove();
                copyAllClassNameList.remove(tempClassName);
                continue;
            }
            // 多级包
            List<String> allParentPackage = getAllParentPackage(tempClassName);
            for(String tempParentPackage : allParentPackage) {
                if (copyAllClassNameList.contains(tempParentPackage)) {
                    RuleNode parentRuleNode = new_className_ruleNode_map.get(tempParentPackage);
                    tempRuleNode.setParentRuleNode(parentRuleNode);// 绑定上级
                    classNameIt.remove();
                    copyAllClassNameList.remove(tempClassName);
                    break;
                }
            }
            if (tempRuleNode.getParentRuleNode() == null) {
                tempRuleNode.setParentRuleNode(rootRuleNode);// 绑定ROOT
                classNameIt.remove();
                copyAllClassNameList.remove(tempClassName);
            }
        }
        className_ruleNode_map.set(new_className_ruleNode_map);
        className_availableRuleList_map.set(new HashMap<>());
    }

    /**
     * 获取所有父级包
     * @param className className
     * @return 所有父级包
     */
    private static List<String> getAllParentPackage(String className) {
        if (!className.contains(".")) {
            return Collections.singletonList(className);
        }
        // 分割包名，得到包的各个部分
        String[] parts = className.split("\\.");
        // 存储父级包名
        List<String> allParentClassNameList = new ArrayList<>();
        // 循环遍历包的各个部分，构建父级包名
        for (int i = 1; i < parts.length; i++) {
            StringBuilder parentName = new StringBuilder();
            for (int j = 0; j < i; j++) {
                parentName.append(parts[j]);
                if (j < i - 1) {
                    parentName.append(".");
                }
            }
            allParentClassNameList.add(parentName.toString());
        }
        Collections.reverse(allParentClassNameList);// 深层包降序
        return allParentClassNameList;
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        return decide(logger, level);
    }

    public static FilterReply decide(Logger logger, Level level) {
        // 获取匹配的规则，按className前缀过滤
//        List<Rule> ruleList = getRules(logger);
        List<Rule> ruleList = getAllParentRule(logger.getName());
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

    private static List<Rule> getAllParentRule(String loggerName) {
        List<Rule> ruleList = className_availableRuleList_map.get().get(loggerName);
        if (ruleList != null) {
            return ruleList;
        }
        RuleNode ruleNode = className_ruleNode_map.get().get(loggerName);
        // 寻找最近的上级
        if (ruleNode == null) {
            List<String> allParentPackage = getAllParentPackage(loggerName);
            if (allParentPackage != null && allParentPackage.size() > 0) {
                for(String tempPackage : allParentPackage) {
                    ruleNode = className_ruleNode_map.get().get(tempPackage);
                    if (ruleNode != null) {
                        break;
                    }
                }
            }
        }
        if (ruleNode == null) {
            return className_ruleNode_map.get().get(ROOT).getAvaiableRuleList();
        }
        List<Rule> allAvaiableRuleList = new ArrayList<>();
        while (ruleNode.getParentRuleNode() != null) {
            allAvaiableRuleList.addAll(ruleNode.getAvaiableRuleList());
            ruleNode = ruleNode.getParentRuleNode();
        }
        className_availableRuleList_map.get().putIfAbsent(loggerName, allAvaiableRuleList);
        return allAvaiableRuleList;
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
