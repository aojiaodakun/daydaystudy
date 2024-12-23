package com.hzk.webserver.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class HzkServletRequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        System.out.println("requestDestroyed");
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        System.out.println("requestInitialized");
    }
}
