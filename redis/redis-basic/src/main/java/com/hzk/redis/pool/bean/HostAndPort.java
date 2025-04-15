package com.hzk.redis.pool.bean;

import java.util.Objects;

public class HostAndPort {
    private String host;
    private int port;

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HostAndPort hostAndPort = (HostAndPort) obj;
        return Objects.equals(port, hostAndPort.port) && Objects.equals(host, hostAndPort.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}

