package com.hzk.webserver.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class HelloServlet implements Servlet {

    private Random random = new Random();

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
     * http://localhost:8081/ierp/hello
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("com.hzk.webserver.servlet.HelloServlet#service");

        HttpServletResponse response = (HttpServletResponse)servletResponse;
        PrintWriter writer = response.getWriter();
        try {
            Thread.currentThread().sleep(1000 * 8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writer.write("hello,hzk_" + random.nextInt(100));
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
