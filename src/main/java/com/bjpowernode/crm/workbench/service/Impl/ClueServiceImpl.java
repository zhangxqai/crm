package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.vo.ClueVo;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);

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
    public List<Clue> getAllList() {

        List<Clue> clueList = clueDao.getAllList();

        return clueList;
    }


}
