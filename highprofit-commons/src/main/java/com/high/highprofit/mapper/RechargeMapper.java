package com.high.highprofit.mapper;

import com.high.highprofit.bean.Recharge;

import java.util.List;

public interface RechargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Recharge row);

    int insertSelective(Recharge row);

    Recharge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Recharge row);

    int updateByPrimaryKey(Recharge row);

    List<Recharge> selectLatelyRecord(Integer id);
}
