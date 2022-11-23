package com.high.highprofit.mapper;

import com.high.highprofit.bean.Income;

public interface IncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Income row);

    int insertSelective(Income row);

    Income selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Income row);

    int updateByPrimaryKey(Income row);
}