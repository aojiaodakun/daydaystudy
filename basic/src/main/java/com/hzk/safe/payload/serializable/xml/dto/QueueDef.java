package com.hzk.safe.payload.serializable.xml.dto;


import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class QueueDef {
    private String name;
    private boolean transactional;
    private boolean duration = true;
    private int maxQueueLength;
    private String appid;
    private boolean lazyInit;
    private boolean sequential;
    private List<ConsumerDef> consumers;
    private boolean partition;

    public QueueDef() {
    }

    @XmlAttribute
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @XmlAttribute
    public boolean isSequential() {
        return this.sequential;
    }

    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }

    public boolean isTransactional() {
        return this.transactional;
    }

    public void setTransactional(boolean transactional) {
        this.transactional = transactional;
    }

    @XmlAttribute
    public int getMaxQueueLength() {
        return this.maxQueueLength;
    }

    public void setMaxQueueLength(int maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
    }

    @XmlAttribute
    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @XmlAttribute
    public boolean isDuration() {
        return this.duration;
    }

    public void setDuration(boolean duration) {
        this.duration = duration;
    }

    @XmlElements({@XmlElement(
            name = "consumer",
            type = ConsumerDef.class
    )})
    public List<ConsumerDef> getConsumers() {
        return this.consumers;
    }

    public void setConsumers(List<ConsumerDef> consumers) {
        this.consumers = consumers;
    }

    public boolean isPartition() {
        return this.partition;
    }

    public void setPartition(boolean partition) {
        this.partition = partition;
    }

    @Override
    public String toString() {
        return "QueueDef{" +
                "name='" + name + '\'' +
                ", appid='" + appid + '\'' +
                ", consumers=" + consumers +
                '}';
    }
}

