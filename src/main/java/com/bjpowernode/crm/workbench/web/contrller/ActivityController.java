package com.bjpowernode.crm.workbench.web.contrller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.*;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到市场活动控制器");

        String path = request.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/activity/getRemarkListById.do".equals(path)){
            getRemarkListById(request,response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }




    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改市场活动备注的信息");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");

        //创建时间，获取当前时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);


        PrintJson.printJsonObj(response,map);


    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加备注的操作");

        String notContent = request.getParameter("notContent");
        String activityId = request.getParameter("activityId");

        //需要生成一个市场活动备注的id
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ar.setActivityId(activityId);
        ar.setNoteContent(notContent);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);


        PrintJson.printJsonObj(response,map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行备注删除操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag =as.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getRemarkListById(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动的id取得备注信息");

       String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> activityRemark = as.getRemarkListById(activityId);

        PrintJson.printJsonObj(response,activityRemark);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到详细信息页的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //转发需要将数据存到session里
        Activity a = as.detail(id);

        request.setAttribute("a",a);

        //转发
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);



    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动修改的操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        //修改时间，获取当前时间
        String editTime = DateTimeUtil.getSysTime();

        //修改人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setCost(cost);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        activity.setDescription(description);
        activity.setEndDate(endDate);
        activity.setStartDate(startDate);
        activity.setName(name);
        activity.setId(id);
        activity.setOwner(owner);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.update(activity);

        PrintJson.printJsonFlag(response,flag);


    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录");

        String id = request.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Map<String,Object> map = activityService.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);
        //

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的删除操作");

        //由于前端穿过的的可能是多个参数的，所以使用获取多个参数，并存再数组中、
        String[] ids = request.getParameterValues("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        System.out.println("dsd");
        boolean flag = activityService.delete(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询市场活动列表操作");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        //由于字符串没有办法计算，要转换为数字
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);

        //每页展示的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        //使用map对这些数据进行封装传到service层
        Map<String,Object> map = new HashMap();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //将封装的map集合传进去，并返回数据给前端

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
        PaginationVo<Activity> vo = activityService.pageList(map);

        PrintJson.printJsonObj(response,vo);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");

        //创建时间，获取当前时间
        String createTime = DateTimeUtil.getSysTime();

        //创建人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setCost(cost);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);
        activity.setDescription(description);
        activity.setEndDate(endDate);
        activity.setStartDate(startDate);
        activity.setName(name);
        activity.setId(id);
        activity.setOwner(owner);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.save(activity);

        PrintJson.printJsonFlag(response,flag);




    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获取用户列表");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = userService.getUserList();

        PrintJson.printJsonObj(response,userList);

    }


}
