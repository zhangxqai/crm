package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.ClueVo;
import com.bjpowernode.crm.workbench.domain.*;
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
        }else if ("/workbench/clue/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/clue/getRemarks.do".equals(path)){
            getRemarks(request,response);
        }else if ("/workbench/clue/saveRemarks.do".equals(path)){
            saveRemarks(request,response);
        }else if ("/workbench/clue/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/clue/updateClueRemark.do".equals(path)){
            updateClueRemark(request,response);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }else if ("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);
        }else if ("/workbench/clue/getActivityListByNameAndByClueId.do".equals(path)){
            getActivityListByNameAndByClueId(request,response);
        }else if ("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if ("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }




    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("进入线索转换的操作");

        String clueId = request.getParameter("clueId");

        String flag = request.getParameter("flag");

        //创建人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String createBy = ((User) request.getSession().getAttribute("user")).getName();


        Tran t = null;
        //判断flag是否有值，如果有值就要创建交易
        if ("a".equals(flag)){

            t = new Tran();
            //之后就要接收前端传过来的值
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();

            //创建时间，获取当前时间
            String createTime = DateTimeUtil.getSysTime();

            //将这些信息封装到对象中
            t.setId(id);
            t.setName(name);
            t.setMoney(money);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1 = cs.convert(clueId,t,createBy);

        if (flag1){

            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");

        }


    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表，根据名称模糊查询");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response,activities);


    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行关联市场活动的操作");

        String cid = request.getParameter("cid");
        String[] aid = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid,aid);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据模糊市场活动名称查询列表）");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activities = as.getActivityListByNameAndByClueId(map);

        PrintJson.printJsonObj(response,activities);


    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行解除关联操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("更新线索id查询关联市场活动的列表");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activity = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response,activity);

    }

    private void updateClueRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入线索备注修改操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");

        //创建时间，获取当前时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setId(id);
        clueRemark.setEditFlag(editFlag);
        clueRemark.setEditBy(editBy);
        clueRemark.setEditTime(editTime);
        clueRemark.setNoteContent(noteContent);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.updateClueRemark(clueRemark);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("cList",clueRemark);
        PrintJson.printJsonObj(response,map);



    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到备注删除操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);

    }

    private void saveRemarks(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入保存备注信息的操作");

        String notContent = request.getParameter("notContent");
        String clueId = request.getParameter("clueId");

        //之后还要生成一个id，一个创建人，一个创建时间
        String id = UUIDUtil.getUUID();
        //创建时间，获取当前时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人是，当前登录的用户 ,通过获取session，再通过获取Attribute存的user拿到登录用户名
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";


        //将这些信息封装到ClueRemark对象中
        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(notContent);
        cr.setClueId(clueId);
        cr.setCreateTime(createTime);
        cr.setCreateBy(createBy);
        cr.setEditFlag(editFlag);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.saveRemark(cr);

        Map<String,Object> map = new HashMap<>();

        map.put("success",flag);
        map.put("clue",cr);
        PrintJson.printJsonObj(response,map);


    }

    private void getRemarks(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到clue备注表中");

        String id = request.getParameter("clueid");


        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<ClueRemark> clueRemarkList = cs.getRemarks(id);

        PrintJson.printJsonObj(response,clueRemarkList);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到删除数据的操作");

        String[] id = request.getParameterValues("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.delete(id);

        PrintJson.printJsonFlag(response,flag);

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
