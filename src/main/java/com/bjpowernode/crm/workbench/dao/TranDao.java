package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    List<Tran> getAllTran(Map<String, Object> map);

    int getAllCountTran(Map<String, Object> map);
}
