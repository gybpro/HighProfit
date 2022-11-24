package com.high.highprofit.service;

import com.high.highprofit.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 产品相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService(version = "1.0.0")
public class ProductServiceImpl implements ProductService{
    private final ProductMapper productMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public ProductServiceImpl(ProductMapper productMapper, RedisTemplate<String, Object> redisTemplate) {
        this.productMapper = productMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getAvgRate() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String avgRate = (String) valueOperations.get("avgRate");
        if (avgRate == null || avgRate.isEmpty()) {
            avgRate = productMapper.getAvgRate();
            valueOperations.set("avgRate", avgRate, 1, TimeUnit.DAYS);
        }
        return avgRate;
    }
}
