package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.*;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.TranServiceImpl;
import com.bjpowernode.crm.workbench.service.TranService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易控制器");

        String path = request.getServletPath();

        if ("/workbench/transaction/getAllTran.do".equals(path)){

            getAllTran(request,response);

        }else if ("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        }else if ("/workbench/transaction/getAllActivity.do".equals(path)){
            getAllActivity(request,response);
        }else if ("/workbench/transaction/getAllActivityByName.do".equals(path)){
            getAllActivityByName(request,response);
        }else if ("/workbench/transaction/getContactsByName.do".equals(path)){
            getContactsByName(request,response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if ("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/transaction/getHistoryByTranId.do".equals(path)){
            getHistoryByTranId(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if ("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询交易阶段数量统计图的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Map<String,Object> map = ts.getCharts();

        PrintJson.printJsonObj(response,map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行改变阶段的操作");
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");

        //修改时间，获取当前时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setMoney(money);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);

        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",t);

        PrintJson.printJsonObj(response,map);

    }

    private void getHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {


        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");

        System.out.println("根据交易Id获取相对于的交易列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> histories = ts.getHistoryByTranId(tranId);

        //将返回来的集合循环遍历出来，获取stage
        for (TranHistory th:histories) {

            //根据每条交易历史，获取每个阶段
            String stage = th.getStage();
            String possibility = pMap.get(stage);

            th.setPossibility(possibility);

        }

        PrintJson.printJsonObj(response,histories);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转页面");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran tran = ts.detail(id);

        //这个传统请求就不能使用这个了
        //PrintJson.printJsonObj(response,tran);

        /*
            阶段t
            阶段和可能性之间的对应关系pMap
        */

        String stage = tran.getStage();
        //这个相当于application
        ServletContext application = this.getServletContext();
        //还可以用
        //ServletContext application1 = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(stage);

        tran.setPossibility(possibility);

        //需要使用转发,转发还需要把数据传到详情转发的页面上
        request.setAttribute("t",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        System.out.println("执行交易添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");//这里只有客户的名称，没有Id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");

        //创建时间，获取当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setName(name);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(t,customerName);

        if (flag){

            //如果添加成功就跳转到交易页

            //转发,转发不用加项目名，在使用转发的时候，一般是需要搭配一下值的，一起转发到新的页面上
            //request.getRequestDispatcher("/workbench/transaction/index.jsp").forward(request,response);

            //重定向，重定向之后是一个全新的路径需要加上项目名的
            response.sendRedirect(request.getContextPath()+"workbench/transaction/index.jsp");

        }




    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表（根据客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response,sList);


    }

    private void getContactsByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到联系人查询");
        String fullname = request.getParameter("fullname");

        TranService as = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Contacts> contacts = as.getContactsByName(fullname);

        PrintJson.printJsonObj(response,contacts);



    }

    private void getAllActivityByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据姓名查询市场活动列表");
        String name = request.getParameter("name");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = new Activity();
        activity.setName(name);
        List<Activity> activities = as.getAllActivityByName(activity);

        PrintJson.printJsonObj(response,activities);


    }

    private void getAllActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到交易创建中的搜索市场活动列表");

        //String pageNoStr = request.getParameter("pageNo");
        //String pageSizeStr = request.getParameter("pageSize");
        //String name = request.getParameter("name");

        //转换数字，不然没有办法计算
        //int pageNo = Integer.valueOf(pageNoStr);
        //int pageSize = Integer.valueOf(pageSizeStr);

        //计算忽略的条数
        //int skipCount = (pageNo - 1) * pageSize;

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //Map<String,Object> map = new HashMap<>();
        //map.put("pageSize",pageSize);
        //map.put("skipCount",skipCount);
        ///*map.put("name",name);*/
        //Map<String,Object> map1 = as.getAllActivity(map);
        List<Activity> activities = as.getAllActivity();

        PrintJson.printJsonObj(response,activities);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转交易添加页面中");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList",uList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

    }

    private void getAllTran(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取交易信息列表");

        //首先要获取前端传过来的数据
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String owner = request.getParameter("create-owner");
        String name = request.getParameter("create-name");
        String customerId = request.getParameter("create-customerId");
        String stage = request.getParameter("create-stage");
        String type = request.getParameter("create-type");
        String source = request.getParameter("create-source");
        String contactsId = request.getParameter("create-contactsId");

        //需要转换为int类型才能进行计算
        int pageNo = Integer.valueOf(pageNoStr);

        int pageSize = Integer.valueOf(pageSizeStr);

        //计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;


        //之后要将这些数据受用map集合封装
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerId",customerId);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsId",contactsId);
        map.put("skipCount",skipCount);

        //调用service层，传数据处理业务
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        PaginationVo<Tran> tListVo = tranService.getAllTran(map);

        PrintJson.printJsonObj(response,tListVo);

    }


}
