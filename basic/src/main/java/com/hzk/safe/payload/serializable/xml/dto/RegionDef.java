package com.hzk.safe.payload.serializable.xml.dto;


import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class RegionDef {
    private String name;
    private String appid;
    private List<QueueDef> queues;

    public RegionDef() {
    }

    @XmlAttribute
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElements({@XmlElement(
            name = "queue",
            type = QueueDef.class
    )})
    public List<QueueDef> getQueues() {
        return this.queues;
    }

    public void setQueues(List<QueueDef> queues) {
        this.queues = queues;
    }

    @XmlAttribute
    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public String toString() {
        return "RegionDef{" +
                "name='" + name + '\'' +
                ", appid='" + appid + '\'' +
                ", queues=" + queues +
                '}';
    }
}

