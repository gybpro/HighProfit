package com.high.highprofit.service;

import com.high.highprofit.mapper.BidInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 投资记录相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService(version = "1.0.0")
public class BidInfoServiceImpl implements BidInfoService {
    private final BidInfoMapper bidInfoMapper;

    public BidInfoServiceImpl(BidInfoMapper bidInfoMapper) {
        this.bidInfoMapper = bidInfoMapper;
    }

    @Override
    public int getTotalMoney() {
        return bidInfoMapper.selectTotalMoney();
    }
}
