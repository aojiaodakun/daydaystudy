package com.hzk.safe.payload.serializable.common.dto;

import java.io.Serializable;

public class RequestContextDTO implements Serializable {

    private String tenantId;
    private String accountId;


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

    @Override
    public String toString() {
        return "RequestContextDTO{" +
                "tenantId='" + tenantId + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
