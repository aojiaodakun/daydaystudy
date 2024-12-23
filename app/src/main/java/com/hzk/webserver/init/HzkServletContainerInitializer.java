package com.hzk.webserver.init;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;

@HandlesTypes({ServerEndpoint.class, ServerApplicationConfig.class, Endpoint.class})
public class HzkServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("HzkServletContainerInitializer#onStartup");
    }
}
