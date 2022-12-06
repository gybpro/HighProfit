package com.high.highprofit.mapper;

import com.high.highprofit.bean.Income;

import java.sql.Date;
import java.util.List;

public interface IncomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Income row);

    int insertSelective(Income row);

    Income selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Income row);

    int updateByPrimaryKey(Income row);

    List<Income> selectLatelyRecord(Integer id);

    List<Income> getReturnRecord(Date today);

    int updateStatus(Integer id, Integer incomeStatus);
}
