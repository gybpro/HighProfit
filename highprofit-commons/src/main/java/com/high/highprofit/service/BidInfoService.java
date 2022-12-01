package com.high.highprofit.service;

import java.util.List;
import java.util.Map;

/**
 * 投资记录相关业务接口
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public interface BidInfoService {
    String getTotalMoney();

    List<Map<String, Object>> getTop3();
}
