package com.high.highprofit.mapper;

import com.high.highprofit.bean.Income;

import java.util.List;

public interface IncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Income row);

    int insertSelective(Income row);

    Income selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Income row);

    int updateByPrimaryKey(Income row);

    List<Income> selectLatelyRecord(Integer id);
}
