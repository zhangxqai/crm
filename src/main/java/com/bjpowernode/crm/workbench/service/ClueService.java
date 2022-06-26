package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.ClueVo;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue c);

    ClueVo<Clue> getClueList(Map<String, Object> map);

    List<Clue> getAllList();
}
