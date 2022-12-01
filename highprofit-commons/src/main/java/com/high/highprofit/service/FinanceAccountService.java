package com.high.highprofit.service;

import java.math.BigDecimal;

/**
 * 财务帐户相关业务接口
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public interface FinanceAccountService {
    BigDecimal getBalance(String token);
}
