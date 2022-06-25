package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);


        User user = userDao.login(map);

        if (user == null){

            throw new LoginException("账号或者密码错误");
        }

        //如果程序执行到这行，说明账号密码正确
        //需要继续验证其他3项信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        //获取当前时间
        String currentTime = DateTimeUtil.getSysTime();
        //比较时间，如果小于0就表示账号已失效，大于0说明账号正常
        if (expireTime.compareTo(currentTime) < 0){

            throw new LoginException("账号已失效");
        }

        //判断锁定状态
        String lockState = user.getLockState();

        if ("0".equals(lockState)){

            throw new LoginException("账号已锁定");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if (!(allowIps.contains(ip))){

            throw new LoginException("ip地址错误");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();

        return userList;
    }
}
