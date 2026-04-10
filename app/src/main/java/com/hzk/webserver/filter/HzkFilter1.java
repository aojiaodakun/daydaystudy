package com.hzk.webserver.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class HzkFilter1 implements Filter {

    static {
        System.out.println("HzkFilter1 static");
    }

    public HzkFilter1(){
        System.out.println("HzkFilter1 construct");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(LocalDateTime.now() + ",HzkFilter1...");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            Thread.currentThread().sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PrintWriter writer = response.getWriter();
        System.out.println(LocalDateTime.now() + ",sleep test");
        writer.write("sleep test\n");
        writer.flush();


//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

}
