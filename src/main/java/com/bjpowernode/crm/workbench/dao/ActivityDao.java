package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> getActivityByCondition(Map map);

    int getTotalByCondition(Map map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
