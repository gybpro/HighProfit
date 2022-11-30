package com.high.highprofit.service;

import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;

    private final String passwordSalt;

    private RedisTemplate<String, Object> redisTemplate;

    public UserServiceImpl(UserMapper userMapper, @Value("${password.salt}") String passwordSalt, RedisTemplate<String, Object> redisTemplate) {
        this.userMapper = userMapper;
        this.passwordSalt = passwordSalt;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int registerUser(String phone, String password) {
        password  = DigestUtils.md5DigestAsHex((passwordSalt + password).getBytes());
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(password);
        user.setAddTime(new Date());
        return userMapper.insertUser(user);
    }

    @Override
    public boolean checkPhone(String phone) {
        return userMapper.selectPhone(phone) < 1;
    }

    @Override
    public User login(String phone, String password) {
        if (password != null) {
            password  = DigestUtils.md5DigestAsHex((passwordSalt + password).getBytes());
        }
        return userMapper.selectUserByPhoneAndPwd(phone, password);
    }

    @Override
    public int logLastLoginTime(Integer id) {
        return userMapper.updateLastLoginTime(id, new Date());
    }

    @Override
    public int verify(Integer id, String idCard, String name) {
        return userMapper.updateIdCardAndName(id, idCard, name);
    }

    @Override
    public String getUserCount() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String userCount = (String) valueOperations.get("userCount");
        if (userCount == null || userCount.isEmpty()) {
            userCount = String.valueOf(userMapper.selectUserCount());
            valueOperations.set("userCount", userCount, 1, TimeUnit.MINUTES);
        }
        return userCount;
    }
}
