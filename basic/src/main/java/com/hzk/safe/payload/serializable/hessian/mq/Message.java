package com.hzk.safe.payload.serializable.hessian.mq;


import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = -6828493763371906552L;

    private String consumeSynchronizeTag;
    private Object body;

    private String transcationTag;

    private long startDeliverTime;
    private int retryTimes;

    private String region;
    private String realQueueName;

    private String kdtxId;
    private long seq;

    private String routeKey;
    private long innerId = 0;
    private long messageTime = 0;

    private boolean isDLXMessage = false;

    public Message() {
        // Do nothing
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getTranscationTag() {
        return transcationTag;
    }

    public void setTranscationTag(String identify) {
        this.transcationTag = identify;
    }

    public String getConsumeSynchronizeTag() {
        return consumeSynchronizeTag;
    }

    public void setConsumeSynchronizeTag(String consumeSynchronizeTag) {
        this.consumeSynchronizeTag = consumeSynchronizeTag;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public long getInnerId() {
        return innerId;
    }

    public void setInnerId(long innerId) {
        this.innerId = innerId;
    }

    public void setMessageTime() {
        messageTime = System.currentTimeMillis();
    }

    public long getMessageTime() {
        return messageTime;
    }

    public boolean isDLXMessage() {
        return isDLXMessage;
    }

    public void setDLXMessage(boolean DLXMessage) {
        isDLXMessage = DLXMessage;
    }

    public String getKdtxId() {
        return kdtxId;
    }

    public void setKdtxId(String kdtxId) {
        this.kdtxId = kdtxId;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRealQueueName() {
        return realQueueName;
    }

    public void setRealQueueName(String realQueueName) {
        this.realQueueName = realQueueName;
    }

    public long getStartDeliverTime() {
        return startDeliverTime;
    }

    public void setStartDeliverTime(long startDeliverTime) {
        this.startDeliverTime = startDeliverTime;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
