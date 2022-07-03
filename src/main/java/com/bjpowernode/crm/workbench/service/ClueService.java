package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.ClueVo;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue c);

    ClueVo<Clue> getClueList(Map<String, Object> map);

    

    Clue detail(String id);

    Map<String, Object> getOneList(String id);

    boolean update(Clue c);

    ClueVo<Clue> getAllList(Map<String, Object> map);

    boolean delete(String[] id);

    List<ClueRemark> getRemarks(String id);

    boolean saveRemark(ClueRemark cr);

    boolean deleteRemark(String id);

    boolean updateClueRemark(ClueRemark clueRemark);

    boolean unbund(String id);

    boolean bund(String cid, String[] aid);


    boolean convert(String clueId, Tran t, String createBy);
}
