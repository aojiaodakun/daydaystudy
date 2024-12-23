package com.hzk.framework.log.slf4j.logback.filter;

import java.util.ArrayList;
import java.util.List;

public class RuleNode {

    private RuleNode parentRuleNode;// 父级Rule，className为空
    private String className;
    private List<Rule> avaiableRuleList = new ArrayList<>();// 有效期Rule集合
    private List<Rule> ignoreRuleList  = new ArrayList<>();// 过期/未开始Rule集合

    public RuleNode getParentRuleNode() {
        return parentRuleNode;
    }

    public void setParentRuleNode(RuleNode parentRuleNode) {
        this.parentRuleNode = parentRuleNode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Rule> getAvaiableRuleList() {
        return avaiableRuleList;
    }

    public void setAvaiableRuleList(List<Rule> avaiableRuleList) {
        this.avaiableRuleList = avaiableRuleList;
    }

    public List<Rule> getIgnoreRuleList() {
        return ignoreRuleList;
    }

    public void setIgnoreRuleList(List<Rule> ignoreRuleList) {
        this.ignoreRuleList = ignoreRuleList;
    }

    @Override
    public String toString() {
        String parentClassName = parentRuleNode == null ? null : parentRuleNode.getClassName();
        return "RuleNode{" +
                "parentClassName=" + parentClassName +
                ", className='" + className + '\'' +
                '}';
    }
}
