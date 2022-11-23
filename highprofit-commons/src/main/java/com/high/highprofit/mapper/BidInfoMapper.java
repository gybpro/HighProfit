package com.high.highprofit.mapper;

import com.high.highprofit.bean.BidInfo;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo row);

    int insertSelective(BidInfo row);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo row);

    int updateByPrimaryKey(BidInfo row);
}