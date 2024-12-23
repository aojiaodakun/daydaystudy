package com.hzk.framework.log.slf4j.logback.filter;

/**
 * 日志过滤规则
 */
public class Rule {
    private int level;
    private String levelStr;
    private long startTime = 1;
    private long endTime;
    private transient int sort = -1;

    private String className;
    private String appId;
    private String appName;
    private String tenantId;
    private String accountId;
    private String formId;
    private String userId;
    private String userName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelStr() {
        return levelStr;
    }

    public void setLevelStr(String levelStr) {
        this.levelStr = levelStr;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getSort() {
        if (sort == -1) {
            updateSort();
        }
        return sort;
    }


    public void updateSort() {
        int sort = className == null ? 0 : className.length() * 100;
        sort += appId == null ? 0 : 1;
        sort += appName == null ? 0 : 1;
        sort += accountId == null ? 0 : 1;
        sort += tenantId == null ? 0 : 1;
        sort += formId == null ? 0 : 1;
        sort += userId == null ? 0 : 1;
        sort += userName == null ? 0 : 1;
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "level=" + level +
                " sort=" + sort +
                (className == null ? "" : " className='" + className + '\'') +
                (appId == null ? "" : " appId='" + appId + '\'') +
                (appName == null ? "" : " appName='" + appName + '\'') +
                (tenantId == null ? "" : " tenantId='" + tenantId + '\'') +
                (accountId == null ? "" : " accountId='" + accountId + '\'') +
                (formId == null ? "" : " formId='" + formId + '\'') +
                (userId == null ? "" : " userId='" + userId + '\'') +
                (userName == null ? "" : " userName='" + userName + '\'') +
                '}';
    }
}
