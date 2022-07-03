package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.Impl.TranServiceImpl;
import com.bjpowernode.crm.workbench.service.TranService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易控制器");

        String path = request.getServletPath();

        if ("/workbench/transaction/getAllTran.do".equals(path)){

            getAllTran(request,response);

        }else if ("/workbench/transaction/xxx.do".equals(path)){
            //xxx(request,response);
        }
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
        String clueSource = request.getParameter("create-clueSource");
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
        map.put("clueSource",clueSource);
        map.put("contactsId",contactsId);
        map.put("skipCount",skipCount);

        //调用service层，传数据处理业务
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        PaginationVo<Tran> tListVo = tranService.getAllTran(map);

        PrintJson.printJsonObj(response,tListVo);

    }


}
