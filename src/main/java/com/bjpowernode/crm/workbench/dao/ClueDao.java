package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    List<Clue> getClueList();

    int getClueListCount();

    List<Clue> getClueByAll(Map<String, Object> map);

    List<Clue> getAllList();
}
