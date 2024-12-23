package com.hzk.webserver.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet implements Servlet {

    public HelloServlet(){
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
     * http://localhost:8080/ierp/hello
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("com.hzk.webserver.servlet.HelloServlet#service");

        HttpServletResponse response = (HttpServletResponse)servletResponse;
        PrintWriter writer = response.getWriter();
        writer.write("hello");
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
