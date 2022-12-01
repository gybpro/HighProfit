package com.high.highprofit.mapper;

import com.high.highprofit.bean.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo row);

    int insertSelective(BidInfo row);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo row);

    int updateByPrimaryKey(BidInfo row);

    int selectTotalMoney();

    List<Map<String, Object>> selectTop3();
}
