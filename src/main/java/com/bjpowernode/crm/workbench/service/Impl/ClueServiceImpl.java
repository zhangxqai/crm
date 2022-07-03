package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.ClueVo;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);




    @Override
    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if (count != 1) {

            flag = false;

        }

        return flag;
    }

    @Override
    public ClueVo<Clue> getClueList(Map<String, Object> map) {

        //获取clue中有多少条数据
        int count = clueDao.getClueListCount();

        //查询出来clue中的数据带有模糊查询的
        List<Clue> clues = clueDao.getClueByAll(map);
        return null;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public Map<String, Object> getOneList(String id) {

        //查询出全部用户列表
        List<User> uList = userDao.getUserList();

        Clue c = clueDao.getOneList(id);

        Map<String,Object> map = new HashMap<>();

        map.put("uList",uList);
        map.put("c",c);

        return map;
    }

    @Override
    public boolean update(Clue c) {

        boolean flag = true;

        int count = clueDao.update(c);

        if (count !=1 ){

            flag = false;
        }

        return flag;
    }

    @Override
    public ClueVo<Clue> getAllList(Map<String, Object> map) {

        //获取clue中有多少条数据
        int total = clueDao.getTotalListCount(map);

        //查询出来clue中的数据带有模糊查询的
        List<Clue> dataList = clueDao.getCluecondtion(map);

        //将返回来的东西封装到vo中

        ClueVo<Clue> vo = new ClueVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean delete(String[] id) {

        boolean flag = true;

        //查询出备注需要删除多少条数据
        int count = clueRemarkDao.getCountByid(id);

        //删除备注，返回受到影响的条数
        int count1 = clueRemarkDao.deleteRemarkById(id);

        if (count != count1){

            flag = false;

        }

        //删除线索信息，返回受到影响的条数
        int count2 = clueDao.delete(id);

        if (count2 != id.length ) {

            flag = false;
        }
        return flag;
    }

    @Override
    public List<ClueRemark> getRemarks(String id) {

        List<ClueRemark> clueRemarks = clueRemarkDao.getRemarks(id);


        return clueRemarks;
    }

    @Override
    public boolean saveRemark(ClueRemark cr) {

        boolean flag = true;

        int count = clueRemarkDao.saveRemark(cr);

        if (count != 1 ){

            flag = false;

        }

        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = clueRemarkDao.deleteRemark(id);

        if (count != 1){

            flag = false;

        }

        return flag;
    }

    @Override
    public boolean updateClueRemark(ClueRemark clueRemark) {

        boolean flag = true;

        int count = clueRemarkDao.updateClueRemark(clueRemark);

        if (count !=1){

            flag = false;

        }


        return flag;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1) {

            flag = false;

        }

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aid) {

        boolean flag = true;

        for (String id : aid) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(id);

            int count = clueActivityRelationDao.bund(car);

            if (count != 1){

                flag = false;
            }


        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();


        boolean flag = true;

        //(1)通过线索id获取线索对象(线索对象当中封装了线索的信息)
        Clue c = clueDao.getById(clueId);

        //(2) 通过线索对象提取客户信息
        //当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);

        //如果cus为空，说明之前是没有这个客户就要新建一个
        if (cus == null){

            cus = new Customer();

            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateBy(createBy);
            cus.setCreateTime(c.getCreateTime());
            cus.setContactSummary(c.getContactSummary());

            //添加客户
            int count1 = customerDao.save(cus);

            if (count1 !=1){
                flag = false;
            }

        }

        //--------------------------------------------------
        //经过第二步处理后，客户的信息我们已经拥有了，将来在处理其他表的时候，如果要使用到客户的id
        //直接使用cus. getId();
        //---------------------------------------------------

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setJob(c.getJob());
        con.setMphone(c.getMphone());
        con.setFullname(c.getFullname());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());

        //添加联系人
        int count2 = contactsDao.save(con);
        if (count2 != 1 ){
            flag = false;
        }

        //--------------------------------------------------
        //第三步处理后，联系人的信息我们已经拥有了，将来在处理其他表的时候，如果要使用到联系人的id
        //直接使用con. getId();
        //---------------------------------------------------

        //(4) 线索备注转换到客户备注以及联系人备注，根据clueId查询出与线索关联的备注信息
        List<ClueRemark> clueRemarks = clueRemarkDao.getListById(clueId);
        for (ClueRemark clueRemark: clueRemarks) {

            //循环出来返回来的集合
            String notContent = clueRemark.getNoteContent();

            //创建一个客户备注的对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(notContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");

            int count3 = customerRemarkDao.save(customerRemark);

            if (count3 !=1){
                flag = false;
            }

            //创建一个联系人备注的对象，添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(notContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");

            int count4 = contactsRemarkDao.save(contactsRemark);

            if (count4 !=1){
                flag = false;
            }

        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);

        //遍历出每一条与市场活动关联的关联关系记录
        for (ClueActivityRelation clueActivityRelation: clueActivityRelationList) {

            //从每一条遍历出来的记录中取出关联的市场活动id
            String activityId = clueActivityRelation.getActivityId();

            //创建 联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());

            //添加联系人与市场活动的关联
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 4){
                flag = false;
            }

        }

        //(6) 如果有创建交易需求，创建一条交易
        if(t != null){

            /*
                t对象在controller里面已经封装好的信息如下:
                     id, money, name, expectedDate, stage, activityId, createBy, createTime
            */
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setNextContactTime(c.getNextContactTime());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());
            t.setContactsId(con.getId());

            //添加交易
            int count6 = tranDao.save(t);
            if (count6 != 1){
                flag = false;
            }

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(t.getExpectedDate());
            th.setMoney(t.getMoney());
            th.setStage(t.getStage());
            th.setTranId(t.getId());

            //添加交易历史
            int count7 = tranHistoryDao.save(th);

            if (count7 != 1){
                flag = false;
            }


        }

        //(8) 删除线索备注
        for (ClueRemark clueRemark: clueRemarks) {

            int count8 = clueRemarkDao.delete(clueRemark);
            if (count8 != 1 ){
                flag = false;
            }

        }

        //(9) 删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation: clueActivityRelationList) {

            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if (count9 != 1){
                flag = false;
            }

        }

        //(10) 删除线索
        int count10 = clueDao.deleteClue(clueId);

        if (count10 != 1){
            flag = false;
        }

        return flag;
    }


}
