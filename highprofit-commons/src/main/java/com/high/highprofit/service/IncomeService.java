package com.high.highprofit.service;

import com.high.highprofit.bean.Income;

import java.util.List;

/**
 * 收益信息相关业务接口
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public interface IncomeService {
    List<Income> getLateLyRecord(String token);
}
