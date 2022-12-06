package com.high.highprofit.service;

import com.high.highprofit.bean.Recharge;
import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.RechargeMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 充值信息相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService(version = "1.0.0")
public class RechargeServiceImpl implements RechargeService{
    private final RechargeMapper rechargeMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public RechargeServiceImpl(RechargeMapper rechargeMapper, RedisTemplate<String, Object> redisTemplate) {
        this.rechargeMapper = rechargeMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Recharge> getLatelyRecord(String token) {
        User user = (User) redisTemplate.opsForValue().get(token);
        Integer id = user.getId();
        return rechargeMapper.selectLatelyRecord(id);
    }

    @Override
    public boolean add(Recharge recharge) {
        return rechargeMapper.insert(recharge) > 0;
    }
}
