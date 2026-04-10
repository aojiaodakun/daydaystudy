package com.hzk.webserver.servlet;

import org.eclipse.jetty.io.EofException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

public class HelloServlet implements Servlet {

    private static final String UTF_8 = "utf-8";

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
        try {
            Thread.currentThread().sleep(1000 * 8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "hello,hzk_" + random.nextInt(100);
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        // 输出1
//        PrintWriter writer = response.getWriter();
//        writer.write(result);
//        writer.flush();

        // 输出2
        try (OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(response.getOutputStream()),
                UTF_8);) {
            writer.write(result);
            writer.flush();
        } catch (EofException e) {
            System.err.println("EofException");
            e.printStackTrace();
        }catch (IOException e) {
            System.err.println("IOException");
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
