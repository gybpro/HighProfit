package com.high.highprofit.service;

import com.high.highprofit.bean.BidInfo;
import com.high.highprofit.bean.Product;
import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.BidInfoMapper;
import com.high.highprofit.util.Assert;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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
}
