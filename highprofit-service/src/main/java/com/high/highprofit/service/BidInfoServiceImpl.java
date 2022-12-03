package com.high.highprofit.service;

import com.high.highprofit.bean.BidInfo;
import com.high.highprofit.bean.Income;
import com.high.highprofit.bean.Product;
import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.BidInfoMapper;
import com.high.highprofit.mapper.FinanceAccountMapper;
import com.high.highprofit.mapper.IncomeMapper;
import com.high.highprofit.mapper.ProductMapper;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    private final FinanceAccountMapper financeAccountMapper;

    private final ProductMapper productMapper;

    private final IncomeMapper incomeMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public BidInfoServiceImpl(BidInfoMapper bidInfoMapper, FinanceAccountMapper financeAccountMapper, ProductMapper productMapper, IncomeMapper incomeMapper, RedisTemplate<String, Object> redisTemplate) {
        this.bidInfoMapper = bidInfoMapper;
        this.financeAccountMapper = financeAccountMapper;
        this.productMapper = productMapper;
        this.incomeMapper = incomeMapper;
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

    @Override
    public List<Map<String, Object>> getTop3() {
        List<Map<String, Object>> maps = bidInfoMapper.selectTop3();
        maps.forEach(map -> {
            String phone = (String) map.get("phone");
            phone = phone.substring(0, 3) + "******" + phone.substring(9);
            map.put("phone", phone);
        });
        return maps;
    }

    @Override
    public List<BidInfo> getLatelyRecord(String token) {
        User user = (User) redisTemplate.opsForValue().get(token);
        Integer id = user.getId();
        List<BidInfo> bidInfos = bidInfoMapper.selectLatelyRecord(id);
        if (bidInfos.size() > 0) {
            bidInfos.forEach(bidInfo -> System.out.println(bidInfo.getProduct()));
        }
        return bidInfos;
    }

    @Override
    public List<BidInfo> getRecordByProdId(Integer prodId) {
        List<BidInfo> bidInfos = bidInfoMapper.selectRecordByProdId(prodId);
        if (bidInfos != null && bidInfos.size() > 0) {
            bidInfos.forEach(bidInfo -> {
                User user = bidInfo.getUser();
                // 处理用户敏感数据
                String phone = user.getPhone();
                phone = phone.substring(0, 3) + "******" + phone.substring(9);
                user = new User();
                user.setPhone(phone);
                bidInfo.setUser(user);
            });
        }
        return bidInfos;
    }

    @Override
    @Transactional // Spring事务管理
    public boolean invest(Integer prodId, String bidMoney, String token) {
        User user = (User) redisTemplate.opsForValue().get(token);
        Integer uid = user.getId();
        BigDecimal money = new BigDecimal(bidMoney);
        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();

        // 扣减余额
        int count = financeAccountMapper.updateAvailableMoney(uid, money);
        Assert.isFlag(count > 0, "账户余额不足");

        // 扣减产品可用余额
        count += productMapper.updateLeftProductMoney(prodId, money);
        Assert.isFlag(count > 1, "产品剩余可投金额不足");

        // 添加投资记录
        BidInfo nowBidInfo = new BidInfo();
        nowBidInfo.setProdId(prodId);
        nowBidInfo.setUid(uid);
        nowBidInfo.setBidMoney(money);
        nowBidInfo.setBidTime(nowDate);
        nowBidInfo.setBidStatus(1);
        count += bidInfoMapper.insertSelective(nowBidInfo);
        Assert.isFlag(count > 2, "系统异常");

        // 判断产品是否满标
        Product product = productMapper.selectByPrimaryKey(prodId);
        if (product.getLeftProductMoney().compareTo(product.getBidMinLimit()) < 0) {
            // 更新产品满标状态并生成收益计划
            // 更新产品满标状态
            product.setProductStatus(2);
            product.setProductFullTime(nowDate);
            count += productMapper.updateByPrimaryKeySelective(product);
            Assert.isFlag(count > 3, "系统异常");

            // 获取投资记录生成收益计划
            List<BidInfo> bidInfos = bidInfoMapper.selectRecordByProdId(prodId);
            bidInfos.forEach(bidInfo -> {
                Income income = new Income();
                income.setBidId(bidInfo.getId());
                income.setUid(uid);
                income.setProdId(prodId);
                BigDecimal recordBidMoney = bidInfo.getBidMoney();
                income.setBidMoney(recordBidMoney);
                income.setIncomeDate(nowDate);
                Double d = product.getCycle() / 12.0;
                BigDecimal cycle = new BigDecimal(d.toString());
                BigDecimal incomeMoney = recordBidMoney
                        .multiply(product.getRate())
                        .divide(BigDecimal.valueOf(100))
                        .multiply(cycle);
                income.setIncomeMoney(incomeMoney);
                income.setIncomeStatus(0);

                Assert.isFlag(incomeMapper.insertSelective(income) > 0, "系统异常");
            });
            return true;
        }
        return true;
    }
}
