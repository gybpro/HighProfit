package com.high.highprofit.service;

import com.high.highprofit.bean.User;
import com.high.highprofit.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 用户相关业务实现类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@DubboService
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int registerUser(User user) {
        return userMapper.insertUser(user);
    }
}
