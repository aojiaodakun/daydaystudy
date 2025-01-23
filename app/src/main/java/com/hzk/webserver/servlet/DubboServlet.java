package com.hzk.webserver.servlet;

import com.hzk.dubbo.factory.ConsumerFactory;
import com.hzk.dubbo.service.DispatchService;
import org.apache.dubbo.config.ReferenceConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DubboServlet implements Servlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * http://localhost:8081/ierp/dubbo?name=hzk
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        PrintWriter writer = response.getWriter();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String name = request.getParameter("name");
        boolean isNumber = false;
        int number = 0;
        try {
            number = Integer.parseInt(name);
            isNumber = true;
        } catch (Exception ignore) {

        }
        // dubbo消费者
        ReferenceConfig<DispatchService> referenceConfig = ConsumerFactory.getCommonReferenceConfig();
        DispatchService dispatchService = referenceConfig.get();
        Object result = dispatchService.invoke("com.hzk.service.ServiceFactory", "dubboMService", "hello",
                new Object[]{isNumber ? number : name});
        writer.write(result.toString());
        writer.flush();
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }

}
