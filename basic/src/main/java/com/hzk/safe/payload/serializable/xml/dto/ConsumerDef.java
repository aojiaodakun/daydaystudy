package com.hzk.safe.payload.serializable.xml.dto;

import javax.xml.bind.annotation.XmlAttribute;

public class ConsumerDef {
    private String className;
    private boolean autoAck;
    private int concurrency = -1;

    public ConsumerDef() {
    }

    @XmlAttribute(
            name = "class"
    )
    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isAutoAck() {
        return this.autoAck;
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public int getConcurrency() {
        return this.concurrency;
    }

    @XmlAttribute
    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    @Override
    public String toString() {
        return "ConsumerDef{" +
                "className='" + className + '\'' +
                ", autoAck=" + autoAck +
                ", concurrency=" + concurrency +
                '}';
    }
}

