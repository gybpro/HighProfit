package com.high.highprofit.service;

import com.high.highprofit.mapper.BidInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

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

    private final RedisTemplate<String, Object> redisTemplate;

    public BidInfoServiceImpl(BidInfoMapper bidInfoMapper, RedisTemplate<String, Object> redisTemplate) {
        this.bidInfoMapper = bidInfoMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getTotalMoney() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String totalMoney = (String) valueOperations.get("totalMoney");
        if (totalMoney == null || totalMoney.isEmpty()) {
            totalMoney = String.valueOf(bidInfoMapper.selectTotalMoney());
            valueOperations.set("totalMoney", totalMoney, 1, TimeUnit.MINUTES);
        }
        return totalMoney;
    }
}