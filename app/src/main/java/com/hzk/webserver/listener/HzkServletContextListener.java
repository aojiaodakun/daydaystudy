package com.hzk.webserver.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HzkServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("contextDestroyed");
    }
}
