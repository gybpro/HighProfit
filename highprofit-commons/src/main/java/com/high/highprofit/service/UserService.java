package com.high.highprofit.service;

import com.high.highprofit.bean.User;

/**
 * 用户相关业务接口
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public interface UserService {
    int registerUser(User user);

    boolean checkPhone(String phone);

    User login(String phone, String password);
}
