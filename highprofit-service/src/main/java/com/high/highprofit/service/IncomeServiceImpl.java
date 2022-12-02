package com.high.highprofit.service;

import com.high.highprofit.bean.BidInfo;
import com.high.highprofit.bean.Income;
import com.high.highprofit.bean.Product;
import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.IncomeMapper;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 收益信息相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService(version = "1.0.0")
public class IncomeServiceImpl implements IncomeService {
    private final IncomeMapper incomeMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public IncomeServiceImpl(IncomeMapper incomeMapper, RedisTemplate<String, Object> redisTemplate) {
        this.incomeMapper = incomeMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Income> getLateLyRecord(String token) {
        User user = (User) redisTemplate.opsForValue().get(token);
        Integer id = user.getId();
        List<Income> incomes = incomeMapper.selectLatelyRecord(id);
        if (incomes.size() > 0) {
            incomes.forEach(income -> {
                System.out.println(income.getProduct());
                System.out.println(income.getBidInfo());
            });
        }
        return incomes;
    }
}
