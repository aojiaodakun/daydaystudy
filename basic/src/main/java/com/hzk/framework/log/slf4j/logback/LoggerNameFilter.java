package com.hzk.framework.log.slf4j.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;


public class LoggerNameFilter extends TurboFilter {

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        String name = logger.getName();
        if (name.startsWith("com.hzk")) {
            return FilterReply.NEUTRAL;
        }
        return FilterReply.DENY;
    }

}

