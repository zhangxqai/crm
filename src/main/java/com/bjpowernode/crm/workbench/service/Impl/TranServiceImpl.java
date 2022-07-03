package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


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
}
