package com.high.highprofit.service;

import com.high.highprofit.bean.Product;
import com.high.highprofit.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
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
            avgRate = productMapper.selectAvgRate();
            valueOperations.set("avgRate", avgRate, 1, TimeUnit.DAYS);
        }
        return avgRate;
    }

    @Override
    public Product getNewUserPro() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Product newUserPro = (Product) valueOperations.get("newUserPro");
        if (newUserPro == null) {
            newUserPro = productMapper.selectNewUserPro();
            valueOperations.set("newUserPro", newUserPro, 10, TimeUnit.SECONDS);
        }
        return newUserPro;
    }

    @Override
    public Product getPrePro(Integer cycle) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Product prePro = (Product) valueOperations.get("prePro");
        if (prePro == null) {
            prePro = productMapper.selectPrePro(cycle);
            valueOperations.set("prePro", prePro, 10, TimeUnit.SECONDS);
        }
        return prePro;
    }

    @Override
    public List<Product> getScatter() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<Product> scatter = (List<Product>) valueOperations.get("scatter");
        if (scatter == null) {
            scatter = productMapper.selectScatter();
            valueOperations.set("scatter", scatter, 10, TimeUnit.SECONDS);
        }
        return scatter;
    }
}
