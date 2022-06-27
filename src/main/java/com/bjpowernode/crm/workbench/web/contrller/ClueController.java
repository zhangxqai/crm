package com.bjpowernode.crm.workbench.web.contrller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.ClueVo;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if ("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/clue/getClueList.do".equals(path)){
            getClueList(request,response);
        }else if ("/workbench/clue/getAllList.do".equals(path)){
            getAllList(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/getOneList.do".equals(path)){
            getOneList(request,response);
        }else if ("/workbench/clue/update.do".equals(path)){
            update(request,response);
        }



    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("线索列表添加操作");

        //获取前端传过来的信息
        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website") ;
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");

        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        //修改时间，获取当前时间
        String editBy = DateTimeUtil.getSysTime();
        //修改人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String editTime = ((User) request.getSession().getAttribute("user")).getName();

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);
        c.setEditBy(editBy);
        c.setEditTime(editTime);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.update(c);

        PrintJson.printJsonFlag(response,flag);



    }

    private void getOneList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("修改列表需要查询");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Map<String,Object> map = cs.getOneList(id);

        PrintJson.printJsonObj(response,map);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue clue = cs.detail(id);

        //转发的
        request.setAttribute("clue",clue);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }

    private void getAllList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("线索列表查询操作");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");


        //由于字符串没有办法计算，要转换为数字
        int pageNo = Integer.valueOf(pageNoStr);

        //由于字符串没有办法计算，要转换为数字
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        //对这些数据封装起来传到service层
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);


        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*
        * 前端要，市场活动信息列表
                查询的总条数

                业务层拿到了以上两项信息之后，如果做返回
                map：后期用到少的时候，就用这个方法

                    map. put( "dataL ist":dataList)
                    map. put("total ":total)
                    PrintJSON map --> json
                    {"total":100, "datalist":[{市场活动1},{2}, {3}]}


                VO ：后期用到多的时候，就用这个方法

                        PaginationVO<T>
                        private int total;
                        private List<T> datalist;

                 分页查询，每一个模块都会有的选泽通用的Vo操作更方便

        */
        ClueVo<Clue> cListVo = cs.getAllList(map);
        System.out.println("执行了第一次");
        PrintJson.printJsonObj(response,cListVo);



    }

    private void getClueList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("线索列表查询操作");

        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");


        //由于字符串没有办法计算，要转换为数字
        int pageNo = Integer.valueOf(pageNoStr);

        //由于字符串没有办法计算，要转换为数字
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        //对这些数据封装起来传到service层
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);


        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*
        * 前端要，市场活动信息列表
                查询的总条数

                业务层拿到了以上两项信息之后，如果做返回
                map：后期用到少的时候，就用这个方法

                    map. put( "dataL ist":dataList)
                    map. put("total ":total)
                    PrintJSON map --> json
                    {"total":100, "datalist":[{市场活动1},{2}, {3}]}


                VO ：后期用到多的时候，就用这个方法

                        PaginationVO<T>
                        private int total;
                        private List<T> datalist;

                 分页查询，每一个模块都会有的选泽通用的Vo操作更方便

        */
        ClueVo<Clue> cListVo = cs.getClueList(map);

        PrintJson.printJsonObj(response,cListVo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索的添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");

        //创建时间，获取当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setContactSummary(contactSummary);
        c.setDescription(description);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setEmail(email);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response,flag);



    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取用户信息列表");

        UserService uList = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> List = uList.getUserList();

        PrintJson.printJsonObj(response,List);

    }


}
