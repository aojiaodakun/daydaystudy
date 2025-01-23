package com.hzk.webserver.listener;

import com.hzk.framework.lifecycle.Service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Iterator;
import java.util.ServiceLoader;

public class HzkServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("contextInitialized");

        //javaSpi实例化所有Service，如mq，dubbo
        ServiceLoader<Service> serviceLoader = ServiceLoader.load(Service.class);
        Iterator<Service> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            Service tempService = iterator.next();
            try {
                tempService.start();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("contextDestroyed");
    }
}
