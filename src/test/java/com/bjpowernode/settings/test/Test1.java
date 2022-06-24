package com.bjpowernode.settings.test;

import com.bjpowernode.crm.util.DateTimeUtil;

public class Test1 {
    public static void main(String[] args) {

        /*String expireTime = "2022-6-15 08:08:08";
        //获取当前时间的方法，这个是之前就已经制作号的工具包
        String currenTime = DateTimeUtil.getSysTime();

        //比较失效时间的，用当前的时间和之前设置的有效时间
        int count = expireTime.compareTo(currenTime);
        System.out.println(count);*/


        /*

        String lockState = "0" ;
        if ("0".equals(lockState)) {
            System.out.println("账号已锁定");
        }*/

        //浏览器端的ip地址
        String ip = "192.168.1.2";

        //允许访问的ip地址
        String allowIps = "192.168.1.1,192.168.1.2";

        if (allowIps.contains(ip)){
            System.out.println("有效的ip地址，允许访问");
        }else {
            System.out.println("ip地址错误，请联系管理员");
        }

    }
}
