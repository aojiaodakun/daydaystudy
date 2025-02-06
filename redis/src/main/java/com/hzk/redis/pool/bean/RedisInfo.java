package com.hzk.redis.pool.bean;


import java.util.Properties;
import java.util.Set;

public class RedisInfo {
    private Set<HostAndPort> hostAndPorts;
    private Properties properties;
    private String password;

    public Set<HostAndPort> getHostAndPorts() {
        return hostAndPorts;
    }

    public void setHostAndPorts(Set<HostAndPort> hostAndPorts) {
        this.hostAndPorts = hostAndPorts;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
