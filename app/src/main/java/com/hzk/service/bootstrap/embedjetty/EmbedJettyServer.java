package com.hzk.service.bootstrap.embedjetty;

import com.hzk.service.bootstrap.BootServer;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.lang.management.ManagementFactory;
import java.net.BindException;

public class EmbedJettyServer implements BootServer {

    @Override
    public void start(String[] args) throws Exception {
        int port = Integer.getInteger("JETTY_PORT", 8081);
        String contextPath = getProperty("JETTY_CONTEXT", "/ierp");
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        QueuedThreadPool threadPool = new QueuedThreadPool(200);
        threadPool.setName("http-request-pool");
        Server server = new Server(threadPool);

        HttpConfiguration httpConfig = new HttpConfiguration();
        setHttpConfiguration(httpConfig);
        ServerConnector connector = new ServerConnector(server,new HttpConnectionFactory(httpConfig));
        connector.setPort(port);
        connector.setHost("127.0.0.1");
        connector.setIdleTimeout(Integer.getInteger("org.eclipse.jetty.http.timeout",30000));
        server.addConnector(connector);

        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);

        WebAppContext context = new WebAppContext();
        context.setContextPath(contextPath);
        String webappPath = EmbedJettyServer.class.getResource("/webapp/web.xml").getPath();
        context.setDescriptor("file:" + webappPath);
        context.setResourceBase("webapp/");
        context.setParentLoaderPriority(true);

        int max = Integer.getInteger("org.eclipse.jetty.server.Request.maxFormContentSize", 200000000).intValue();
        context.setMaxFormContentSize(max);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", max);

        ContextHandlerCollection handler = new ContextHandlerCollection();
        handler.addHandler(context);
        server.setHandler(handler);
        try {
            server.start();
        } catch (BindException ex) {
            ex.printStackTrace();// NOSONAR
            System.exit(-1);
        }

        Throwable t = context.getUnavailableException();
        if (t != null) {
            throw new Error("WebContext start failed:" + t.getMessage(), t);
        } else if (context.isFailed()) {
            throw new Error("WebContext start failed.");
        }
//        主线程卡住
        server.join();
    }

    private static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        value = System.getenv(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    private static void setHttpConfiguration(HttpConfiguration httpConfig){
        Integer requestHeaderSize = Integer.getInteger("org.eclipse.jetty.request.header.size", 8192);
        httpConfig.setRequestHeaderSize(requestHeaderSize);
        Integer responseHeaderSize = Integer.getInteger("org.eclipse.jetty.response.header.size", 8192);
        httpConfig.setResponseHeaderSize(responseHeaderSize);
        Integer headerCacheSize = Integer.getInteger("org.eclipse.jetty.header.cache.size", 1024);
        httpConfig.setHeaderCacheSize(headerCacheSize);
    }

}
