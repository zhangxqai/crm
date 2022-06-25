package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<>();

        //将字典类型取出
        List<DicType> dicTypes = dicTypeDao.getTypeList();

        //将字典类型列表遍历
        for (DicType dt: dicTypes){
            //获取每一种类型的字典类型编码
            String code = dt.getCode();

            //通过获取到的code值，再到value表中查询出来字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code +"List", dvList);

        }

        return map;
    }
}
