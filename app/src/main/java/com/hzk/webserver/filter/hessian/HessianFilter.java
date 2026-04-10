package com.hzk.webserver.filter.hessian;


import com.alibaba.com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.io.Hessian2Input;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证hessain反序列化漏洞
 * com.caucho.hessian.io.Hessian2Input#readObjectDefinition(java.lang.Class)
 * 限制点：com.caucho.hessian.io.Hessian2Input#allow
 */
public class HessianFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        System.out.println("HessianFilter...");

        try (ServletInputStream inputStream = request.getInputStream()){
            // 原生hessian
            //            mH2i.allow("com.hzk.webserver.filter.hessian.CommonRpcParam");// 历史遗留接口，无效
            Hessian2Input mH2i = new Hessian2Input(inputStream);
            mH2i.setSerializerFactory(NativeHessianSerializerFactory.SERIALIZER_FACTORY);
            CommonRpcParam commonRpcParam = (CommonRpcParam)mH2i.readObject();
            System.out.println(commonRpcParam);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(e.getMessage());
        }
    }

    @Override
    public void destroy() {

    }


}
