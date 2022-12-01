package com.high.highprofit.mapper;

import com.high.highprofit.bean.User;

import java.util.Date;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    int insertUser(User user);

    int selectPhone(String phone);

    User selectUserByPhoneAndPwd(String phone, String password);

    int updateLastLoginTime(Integer id, Date lastLoginTime);

    int updateIdCardAndName(Integer id, String idCard, String name);

    int selectUserCount();

    int updateHeaderImage(Integer id, String headerImage);
}
