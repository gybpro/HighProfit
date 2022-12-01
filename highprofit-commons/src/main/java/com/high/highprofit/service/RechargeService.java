package com.high.highprofit.service;

import com.high.highprofit.bean.Recharge;

import java.util.List;

/**
 * 充值信息相关业务接口
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public interface RechargeService {
    List<Recharge> getLatelyRecord(String token);
}
