package com.hzk.webserver.filter;

import javax.servlet.*;
import java.io.IOException;

public class HzkFilter1 implements Filter {

    public HzkFilter1(){
        System.out.println("HzkFilter1 construct");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("HzkFilter1...");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

}
