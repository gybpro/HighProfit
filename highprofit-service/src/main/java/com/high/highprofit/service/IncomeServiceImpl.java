package com.high.highprofit.service;

import com.high.highprofit.bean.Income;
import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.FinanceAccountMapper;
import com.high.highprofit.mapper.IncomeMapper;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.Date;
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

    private final FinanceAccountMapper financeAccountMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public IncomeServiceImpl(IncomeMapper incomeMapper, FinanceAccountMapper financeAccountMapper,
                             RedisTemplate<String, Object> redisTemplate) {
        this.incomeMapper = incomeMapper;
        this.financeAccountMapper = financeAccountMapper;
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

    // 几号和周几会有冲突，所以其中一个值要用?指定
    // ?在cron表达式中是表示没有具体的值(无值)，?也只能在这两个中一个用
    // cron表达式的表示顺序是，秒分时日月周年，日是一个月的第几日，周是一周的第几天
    @Scheduled(cron = "0 * 14 * * ?")
    public void returnMoney() {
        // 查询今日返现且返现状态为0(未返现)的收益记录
        java.sql.Date today = new java.sql.Date(new Date().getTime());
        List<Income> incomes = incomeMapper.getReturnRecord(today);
        // 将收益计划中的本金和利息返还给用户资金账户，并将返现状态改为1(已返现)
        if (incomes == null || incomes.isEmpty()) {
            return;
        }
        incomes.forEach(income -> {
            BigDecimal returnMoney = income.getIncomeMoney().add(income.getBidMoney());
            Integer uid = income.getUid();
            Assert.isFlag(financeAccountMapper.updateAvailableMoneyByReturnMoney(uid, returnMoney) > 0, "系统异常");
            Assert.isFlag(incomeMapper.updateStatus(income.getId(), 1) > 0, "系统异常");
        });
    }
}
