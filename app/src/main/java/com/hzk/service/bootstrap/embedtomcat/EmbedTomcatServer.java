package com.hzk.service.bootstrap.embedtomcat;

import com.hzk.service.bootstrap.BootServer;

import com.hzk.service.bootstrap.embedjetty.EmbedJettyServer;
import org.apache.catalina.Context;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.IOException;


public class EmbedTomcatServer implements BootServer {

    public static final String SERVER_CONNECTION_TIMEOUT = "server.connection-timeout";
    public static final String SERVER_MAX_HTTP_HEADER_SIZE = "server.max-http-header-size";
    public static final String SERVER_AAS_MAX_THREADS = "server.tomcat.max-threads";
    private static final String ENCODING_FILTER = "encodingFilter";


    private static void initWebappDefaults(Tomcat tomcat, Context context) {
        Wrapper servlet = tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
        servlet.setLoadOnStartup(1);
        servlet.setOverridable(true);
        context.addServletMappingDecoded("/", "default");
        context.setSessionTimeout(30);
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

    @Override
    public void start(String[] args) {
        try {
            int port = Integer.getInteger("JETTY_PORT", 8081);
            String contextPath = getProperty("JETTY_CONTEXT", "/ierp");
            if (!contextPath.startsWith("/")) {
                contextPath = "/" + contextPath;
            }
            String webappPath = EmbedJettyServer.class.getResource("/webapp").getPath();
            //Integer maxThreads =Integer.parseInt( getProperty("JETTY_MAXTHREADS", "200"));
            Tomcat tomcat = new Tomcat();
            tomcat.getHost().setAutoDeploy(false);
            tomcat.setAddDefaultWebXmlToWebapp(false);//关闭之后才会去加载自定义的web.xml
            ContextConfig contextConfig = new ContextConfig();
            String source = EmbedJettyServer.class.getResource("/webapp/web.xml").getPath();
            if(source.indexOf("target/classes") > 0){
                contextConfig.setDefaultWebXml("file:" + source);
            } else {
                contextConfig.setDefaultWebXml("jar:" + source);
            }
            Context ierpContext = tomcat.addWebapp(tomcat.getHost(), contextPath, webappPath, contextConfig);
//            StandardJarScanner scannerIerp = (StandardJarScanner)ierpContext.getJarScanner();
//            scannerIerp.setScanManifest(false); //若为true tomcat8.5之后会默认加载jar包里MANIFEST.MF的classpath,出现jar包找不到的提示错误
            StandardJarScanner scannerIerp = (StandardJarScanner)ierpContext.getJarScanner();
            scannerIerp.setJarScanFilter(new StandardJarScanFilter(){//默认会扫描所有jar包下面的web-fragement.xml,我们只有一个web.xml，不需要开放这个特性，大大加快启动速度
                public boolean isSkipAll() {
                    return true;
                }
            });
            // websocket服务
//            ierpContext.addApplicationListener("kd.bos.msgjet.websocket.tomcat.TomcatWsContextListener");
            /* tomcat、aas、bes都引入了websocket的api的依赖包，(tomcat-websocket、aas-websocket、bes-websocket) 会导致websocket实现被自动加载n次，从而会导致异常
             * 故需要修改EmbedTomcatServer应用上下文过滤掉对aas和bes的websocket的加载
             * 注意：springboot启动模式下也需要排除，在TomcatServletWebServerFactoryImpl修改
             * */
            String containerSciFilter = "org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer|" +
                    "org.eclipse.jetty.websocket.server.NativeWebSocketServletContainerInitializer|" +
                    "com.apusic.aas.websocket.server.WsSci|" +
                    "com.tongweb.web.websocket.server.WsSci|" +
                    "com.bes.enterprise.web.websocket.server.WsSci|" +
                    "com.bes.enterprise.web.jasper.servlet.JasperInitializer";
            ierpContext.setContainerSciFilter(containerSciFilter);
            ierpContext.setParentClassLoader(EmbedTomcatServer.class.getClassLoader());

            initWebappDefaults(tomcat, ierpContext); //初始化url为"/"的servlet

            StandardContext monitorContext = new StandardContext();
            monitorContext.setPath("");
            StandardJarScanner jarScanner = (StandardJarScanner)monitorContext.getJarScanner();
            jarScanner.setJarScanFilter(new StandardJarScanFilter(){//默认会扫描所有jar包下面的web-fragement.xml,我们只有一个web.xml，不需要开放这个特性，大大加快启动速度
                public boolean isSkipAll() {
                    return true;
                }
            });
            monitorContext.setContainerSciFilter(containerSciFilter);
            monitorContext.addLifecycleListener(new Tomcat.FixContextListener()); //该监听器在不使用web.xml时为必须项
            tomcat.getHost().addChild(monitorContext);

            int max = Integer.getInteger("server.tomcat.max-http-form-post-size", 200000000).intValue();
            Connector connector = new Connector();
            configConenctor(connector);
            connector.setPort(port);
            connector.setMaxPostSize(max);
            tomcat.setConnector(connector);
            tomcat.start();

            // 隐藏版本号
            Valve[] valveArray = tomcat.getHost().getPipeline().getValves();
            for (Valve tempValve : valveArray) {
                String simpleName = tempValve.getClass().getSimpleName();
                if (simpleName.equals("ErrorReportValve")) {
                    ErrorReportValve errorReportValve = (ErrorReportValve)tempValve;
                    errorReportValve.setShowServerInfo(false);
                    break;
                }
            }
        } catch (Exception | Error e) {
            throw new Error("EmbedTomcatServer start exception:" + e.getMessage(), e);
        }
    }

    private void configConenctor(Connector connector) {
        ProtocolHandler handler = connector.getProtocolHandler();

        String _connectionTimeout = System.getProperty(SERVER_CONNECTION_TIMEOUT, System.getenv(SERVER_CONNECTION_TIMEOUT));
        String _maxHttpHeaderSize = System.getProperty(SERVER_MAX_HTTP_HEADER_SIZE, System.getenv(SERVER_MAX_HTTP_HEADER_SIZE));
        String _maxThreads = System.getProperty(SERVER_AAS_MAX_THREADS, System.getenv(SERVER_AAS_MAX_THREADS));

        if (_connectionTimeout !=null && _connectionTimeout.length()>0){
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol<?> protocol = (AbstractProtocol<?>) handler;
                protocol.setConnectionTimeout(Integer.parseInt(_connectionTimeout));
            }
        }

        if (_maxHttpHeaderSize !=null && _maxHttpHeaderSize.length()>0){
            if (handler instanceof AbstractHttp11Protocol) {
                AbstractHttp11Protocol protocol = (AbstractHttp11Protocol) handler;
                protocol.setMaxHttpHeaderSize(Integer.parseInt(_maxHttpHeaderSize));
            }
        }

        if ( _maxThreads !=null &&  _maxThreads.length()>0){
            if (handler instanceof AbstractProtocol) {
                AbstractProtocol<?> protocol = (AbstractProtocol<?>) handler;
                protocol.setMaxThreads(Integer.parseInt( _maxThreads));
            }
        }
    }

}

