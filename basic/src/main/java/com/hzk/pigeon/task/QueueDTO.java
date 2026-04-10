package com.hzk.pigeon.task;

public class QueueDTO {

    private String queueName;
    private String msg;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "QueueDTO{" +
                "queueName='" + queueName + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
