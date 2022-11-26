package com.high.highprofit.mapper;

import com.high.highprofit.bean.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    int insertUser(User user);

    int selectPhone(String phone);
}
