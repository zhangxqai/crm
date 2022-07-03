package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getRemarks(String id);

    int saveRemark(ClueRemark cr);

    int getCountByid(String[] id);

    int deleteRemarkById(String[] id);

    int deleteRemark(String id);

    int updateClueRemark(ClueRemark clueRemark);

    List<ClueRemark> getListById(String clueId);

    int delete(ClueRemark clueRemark);
}
