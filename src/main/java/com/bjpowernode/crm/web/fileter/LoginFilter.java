package com.bjpowernode.crm.web.fileter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到验证有没有登录过的过滤器");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String path = request.getServletPath();

        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){

            //不应该拦截的资源，应该放行
            chain.doFilter(servletRequest, servletResponse);

        }else {

            //其他资源的都需要判断
            if (user != null){

                chain.doFilter(servletRequest, servletResponse);
            }else {
                //重定向到登录页面，
                //重定向和转发都是可以跳转到指定的页面中
                //重定向和转发的路径都是要绝对路径的
            /*
            转发:
                使用的是一种特殊的绝对路径的使用方式， 这种绝对路径前面不加/项目名，这种路径也称之为内部路径
                /login. jsp


            重定向:
                使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                /crm/Login. jsp

             */
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }

        }


    }
}
