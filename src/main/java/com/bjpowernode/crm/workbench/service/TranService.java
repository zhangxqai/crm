package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.Map;

public interface TranService {
    PaginationVo<Tran> getAllTran(Map<String, Object> map);
}
