package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);



    @Override
    public boolean save(Activity activity) {

        boolean flag = true;

        int count = activityDao.save(activity);

        if (count != 1 ) {

            flag = false;

        }

        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map map) {

        //获取total
        int total = activityDao.getTotalByCondition(map);

        //获取dataList
        List<Activity> dataList = activityDao.getActivityByCondition(map);

        //创建vo对象，将total和dataList封装到对象中
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;
        //查询出需要删除的备注的数量,看看数组传过来有多少个，有可能是一个，有可能是一个
        int count = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回受到影响的条数
        int count1 = activityRemarkDao.deleteByAids(ids);

        if(count != count1){

            flag = false;

        }

        //删除市场活动
        int count3 = activityDao.delete(ids);

        if (count3 != ids.length){

            flag = false;

        }

        return flag;

    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> uList = userDao.getUserList();
        //取a，市场活动单条
        Activity a = activityDao.getById(id);

        //将uList 和 a 打包返回给界面层
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);

        return map;
    }

    @Override
    public boolean update(Activity activity) {

        boolean flag = true;

        int count = activityDao.update(activity);

        if (count != 1 ) {

            flag = false;

        }

        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListById(String activityId) {

        List<ActivityRemark> activityRemarks = activityRemarkDao.getRemarkListById(activityId);
        return activityRemarks;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteRemarkById(id);

        if (count != 1) {

            flag = false;

        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count != 1 ){

            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count != 1 ){

            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> activities = activityDao.getActivityListByClueId(clueId);

        return activities;
    }

    @Override
    public List<Activity> getActivityListByNameAndByClueId(Map<String, String> map) {

        List<Activity> activities = activityDao.getActivityListByNameAndByClueId(map);

        return activities;
    }

    @Override
    public List<Activity> getAllActivity() {

        List<Activity> activities = activityDao.getAllActivity();

        return activities;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {

        List<Activity> activities = activityDao.getActivityListByName(aname);

        return activities;
    }

    @Override
    public List<Activity> getAllActivityByName(Activity activity) {

        List<Activity> activities = activityDao.getAllActivityByName(activity);

        return activities;
    }



}
