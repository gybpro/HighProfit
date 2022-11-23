package com.high.highprofit.mapper;

import com.high.highprofit.bean.Recharge;

public interface RechargeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Recharge row);

    int insertSelective(Recharge row);

    Recharge selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Recharge row);

    int updateByPrimaryKey(Recharge row);
}