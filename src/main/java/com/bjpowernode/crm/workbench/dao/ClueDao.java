package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

    public interface ClueDao {


    int save(Clue c);


    int getClueListCount();

    List<Clue> getClueByAll(Map<String, Object> map);

    Clue detail(String id);

    Clue getOneList(String id);

    int update(Clue c);

    List<Clue> getCluecondtion(Map<String, Object> map);

    int getTotalListCount(Map<String, Object> map);

        int delete(String[] id);

        Clue getById(String clueId);

        int deleteClue(String clueId);
    }
