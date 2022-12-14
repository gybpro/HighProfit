package com.high.highprofit.mapper;

import com.high.highprofit.bean.FinanceAccount;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount row);

    int insertSelective(FinanceAccount row);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount row);

    int updateByPrimaryKey(FinanceAccount row);

    BigDecimal selectAvailableMoneyByUserId(Integer id);

    int updateAvailableMoney(Integer uid, BigDecimal money);

    int updateAvailableMoneyByReturnMoney(Integer uid, BigDecimal returnMoney);
}
