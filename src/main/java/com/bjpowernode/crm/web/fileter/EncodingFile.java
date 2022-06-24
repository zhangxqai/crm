package com.bjpowernode.crm.web.fileter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFile implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到过滤字符编码的过滤器");

        //过滤post请求的中文参数乱码
        servletRequest.setCharacterEncoding("UTF-8");

        //过滤响应流响应乱码
        servletResponse.setContentType("text/html;charset=utf-8");

        //将请求放行
        chain.doFilter(servletRequest,servletResponse);

    }
}
