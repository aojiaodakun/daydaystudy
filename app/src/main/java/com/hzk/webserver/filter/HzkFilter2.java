package com.hzk.webserver.filter;

import javax.servlet.*;
import java.io.IOException;

public class HzkFilter2 implements Filter {

    public HzkFilter2(){
        System.out.println("HzkFilter2 construct");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("HzkFilter2...");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
