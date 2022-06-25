package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {


    /*
    * 这个方法是用来监听上下文域对象的，当服务器启动了就会，上下文域对象就会创建，创建好了之后，就会马上执行这个方法
    *
    * event：该参数能够取得监听的对象
    *           监听的是什么对象，就可以通过这个参数取得什么对象
    *           例如我们监听的是上下文域对象，就会取得上下文域对象，
    * */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("服务器缓存处理数据字典开始");
        ServletContext application = event.getServletContext();

        //取得数据字典
        //........
        //application.setAttribute(key,数字字典);

        //现在就要取到数字字典，这样就要先查询，再返回来
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        //先通过这个方法查到数据，并需要返回什么数据
        /*
        * 可以打包成为一.个map
            业务层应该是这样来保存数据的:
            map. put( "appellationlist", dvlist1);
            map. put("clueStatelist", dvlist2);
            map. put( "stagelist", dvList3);
         */
        Map<String, List<DicValue>> map = ds.getAll();

        //将map解析为上下文域对象中保存的键值对
        //这样就可以取出map中的所有key值
        Set<String> set= map.keySet();

        //再通过这些key值循环遍历出来value值，并将这些值存到application里面
        for (String key : set) {
            application.setAttribute(key , map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");

    }
}
