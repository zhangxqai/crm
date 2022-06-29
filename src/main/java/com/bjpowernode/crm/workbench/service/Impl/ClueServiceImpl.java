package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.vo.ClueVo;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.dao.ClueRemarkDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

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


}
