package com.bjpowernode.crm.settings.web.contrller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.MD5Util;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户控制器");

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){

             login(request,response);

        }/*else if ("/settings/user/xx.do".equals(path)){

        }*/

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到验证操作");

        //获取前端输入的账号和密码
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        //由于获取到的密码是明文的方式，需要转换为密文才能验证成功
        loginPwd = MD5Util.getMD5(loginPwd);

        //接收浏览器端的一个ip地址
        String ip = request.getRemoteAddr();
        System.out.println("ip======:"+ ip);

        //获取service对象,,未来业务层开发，都是统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        System.out.println("1111");

        try{

            //如果验证成功，就会返回user对象，并且将这个保存到session域中，为了方便以后登录是要用到
            //
            //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<");
            User user = us.login(loginAct,loginPwd,ip);

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            //将user对象保存到session对象
            request.getSession().setAttribute("user",user);

            //如果执行到这里，就说明业务层是没有问题的，没有抛出异常的
            //表示登录成功

            /*
            * {"success" : true}
            * */
            PrintJson.printJsonFlag(response,true);

            //System.out.println("______________________");
        }catch (Exception e){
            e.printStackTrace();
            //如果程序执行到这里，就说明业务层已经验证失败，会抛出异常的 ，就表示登录失败了
            /*
            *
            * {"success" : true,"msg" ：？}
            *
            * */
            String msg = e.getMessage();

            Map<String , Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);

            PrintJson.printJsonObj(response,map);
        }

    }
}
