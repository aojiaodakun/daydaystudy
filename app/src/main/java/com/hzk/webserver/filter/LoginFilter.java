package com.hzk.webserver.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginFilter implements Filter {

    // 账号密码
    static Map<String, String> user_password_map = new HashMap<String, String>() {
        {
            put("admin", "admin");
        }
    };
    // 已登录会话，TODO 放redis
    static Set<String> sessionIdSet = new HashSet<>();

    public LoginFilter() {
        System.out.println("LoginFilter construct");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    /**
     * http://127.0.0.1:8080/ierp/login.do?user=admin&password=admin
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        System.out.println("LoginFilter...");
        String user = request.getParameter("user");
        String password = request.getParameter("password");

        String requestURI = request.getRequestURI();

        if (this.isWhiteListPath(requestURI)) {
            filterChain.doFilter((ServletRequest)servletRequest, servletResponse);
            return;
        }
        if (requestURI.endsWith("login.do")) {
            if (user == null || user.equals("")
                    || password == null || password.equals("")) {
                PrintWriter writer = response.getWriter();
                writer.write("please login\n");
                writer.flush();
                return;
            } else {
                boolean flag = user_password_map.get(user).equals(password);
                PrintWriter writer = response.getWriter();
                if (flag) {
                    Cookie userCookie = new Cookie("sessionId", user);
                    userCookie.setPath("/");
                    sessionIdSet.add(user);
                    response.addCookie(userCookie);
                    writer.write("login success\n");
                    writer.flush();
                    return;
                } else {
                    writer.write("user or password error\n");
                    writer.flush();
                    return;
                }
            }
        }
        // cookie校验会话
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            PrintWriter writer = response.getWriter();
            writer.write("please login\n");
            writer.flush();
            return;
        }
        boolean isLogin = false;
        for (Cookie tempCookie : cookies) {
            String name = tempCookie.getName();
            String value = tempCookie.getValue();
            if (name.equals("sessionId")) {
                // 校验是否登录过
                if (sessionIdSet.contains(value)) {
                    isLogin = true;
                    break;
                }else{
                    PrintWriter writer = response.getWriter();
                    writer.write("please login\n");
                    writer.flush();
                    return;
                }
            }
        }
        if (isLogin) {
            // 放行
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
    private boolean isWhiteListPath(String path) {
        boolean isWhiteListPath = path.contains("/property") ;
        return isWhiteListPath  ? true : isWhiteListPath;
    }


    @Override
    public void destroy() {

    }
}
