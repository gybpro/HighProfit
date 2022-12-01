package com.high.highprofit.service;

import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.FinanceAccountMapper;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * 财务帐户相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService(version = "1.0.0")
public class FinanceAccountServiceImpl implements FinanceAccountService {
    private final FinanceAccountMapper financeAccountMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public FinanceAccountServiceImpl(FinanceAccountMapper financeAccountMapper, RedisTemplate<String, Object> redisTemplate) {
        this.financeAccountMapper = financeAccountMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public BigDecimal getBalance(String token) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        User user = (User) valueOperations.get(token);
        Assert.isFlag(user != null, "用户未登录，请前往登录");
        Integer id = user.getId();
        BigDecimal balance = (BigDecimal) valueOperations.get(id + ":balance");
        if (balance == null) {
            balance = financeAccountMapper.selectAvailableMoneyByUserId(id);
            valueOperations.set(id + ":balance", balance, 10, TimeUnit.SECONDS);
        }
        return balance;
    }
}
