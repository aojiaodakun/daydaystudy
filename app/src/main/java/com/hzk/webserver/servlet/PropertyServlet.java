package com.hzk.webserver.servlet;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PropertyServlet implements Servlet {

    public PropertyServlet() {
        System.out.println("com.hzk.webserver.servlet.HelloServlet#construct");
    }


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * http://127.0.0.1:8081/ierp/property.do?name=java.version
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("com.hzk.webserver.servlet.PropertyServlet#service");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String name = request.getParameter("name");
        String property = System.getProperty(name);
        /**
         * 返回系统的所有属性
         */
//        String property = String.valueOf(System.getProperties());
        PrintWriter writer = response.getWriter();
        writer.write(property);
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
