package com.hzk.safe.payload.serializable.xml.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(
        name = "root"
)
public class UsageConfig {

    private List<RegionDef> regions;
    @XmlElements(value =
            {@XmlElement(name = "region", type = RegionDef.class)})
    public List<RegionDef> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionDef> regions) {
        this.regions = regions;
    }

    @Override
    public String toString() {
        return "UsageConfig{" +
                "regions=" + regions +
                '}';
    }
}
