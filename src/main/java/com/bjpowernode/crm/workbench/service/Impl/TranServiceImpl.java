package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public PaginationVo<Tran> getAllTran(Map<String, Object> map) {

        //根据map，查询出有多少条数据
        int total = tranDao.getAllCountTran(map);

        //根据map，查询出每一条的数据
        List<Tran> dataList = tranDao.getAllTran(map);

        PaginationVo paginationVo = new PaginationVo();

       paginationVo.setTotal(total);
        paginationVo.setDataList(dataList);

        return paginationVo;
    }

    @Override
    public List<Contacts> getContactsByName(String fullname) {

        List<Contacts> contacts = contactsDao.getContactsByName(fullname);

        return contacts;
    }

    @Override
    public boolean save(Tran t, String customerName) {

        boolean flag = true;

        /*

            交易添加业务：

                在添加之前，参数t里面就少了三个主键，分别是String customerName, String activityName, String contactsName

                先处理这些需求

                (1)判断customerName，根据客户名称在客户表进行精确查询
                    如果有这个客户，则取出这个客户的id,封装到t对象中
                    如果没有这个客户，则再客户表新建一条客户信息，然后将新建的客户的id取出，封装到t对象中

                (2)经过以上操作后，t对象中的信息就全了，需要执行添加交易的操作

                (3)添加交易完毕后，需要创建一条交易历史.


        */

        Customer cus = customerDao.getCustomerByName(customerName);

        if (cus != null){

            //如果cus为null，就需要创建客户
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setDescription(t.getDescription());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());

            //添加客户
            int count1 = customerDao.save(cus);
            if (count1 != 1){

                flag = false;

            }
        }
        //通过以，上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户已经有了，客户的id就有了
        t.setCustomerId(cus.getId());

        //添加交易
        int count2 =  tranDao.save(t);

        if (count2 !=1){

            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        int count3 = tranHistoryDao.save(th);

        if (count3 != 1){

            flag = false;

        }


        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran tran = tranDao.detail(id);

        return tran;
    }

    @Override
    public List<TranHistory> getHistoryByTranId(String tranId) {

        List<TranHistory> histories = tranHistoryDao.getHistoryByTranId(tranId);

        return histories;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;
        int count = tranDao.changeStage(t);

        if (count != 1){

            flag = false;

        }

        //交易阶段修改之后就要生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());

        //添加交易历史
        int count2 = tranHistoryDao.save(th);


        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //获取total
        int total = tranDao.getTotal();

        //获取dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //将total和dataList封装map中
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
